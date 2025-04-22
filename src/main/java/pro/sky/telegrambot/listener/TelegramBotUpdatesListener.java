package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskService service;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService service) {
        this.telegramBot = telegramBot;
        this.service = service;
    }

    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    //работа с входящими сообщениями
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Matcher matcher = pattern.matcher(update.message().text());
            if (update != null && update.message().text().equals("/start")) {
                sendMessage(update.message().chat().id(), "Приветствую тебя, друг, в Планировщик-Боте!");
            } else if (matcher.matches()) {
                String date = matcher.group(1);
                String textNotification = matcher.group(3);

                LocalDateTime dateNotification = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                LocalDateTime dateCurrent = LocalDateTime.now();

                if (dateNotification.isBefore(dateCurrent)) {
                    sendMessage(update.message().chat().id(), "Друг, ты указал дату меньше текущей! Я не могу сохранить твою напоминалку :(");
                } else {
                    service.addNotification(update.message().chat().id(), textNotification, dateNotification);
                    sendMessage(update.message().chat().id(), "Твоя напоминалка сохранена!");
                }
            } else {
                sendMessage(update.message().chat().id(), "Друг, твоя напоминалка не соответствует заданному формату.");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    // метод по отправке напоминалок в указанное время
    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification() {
        LocalDateTime dateCurrent = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> allNotification = service.getNotifications(dateCurrent);
        allNotification.forEach(notificationTask ->
        {
            String message = "Друг, у тебя запланировано - " + notificationTask.getTextNotification();
            sendMessage(notificationTask.getIdChat(), message);
        });
    }

    // метод по отправке исходящих сообщений
    private void sendMessage(Long idChat, String textMessage) {
        SendMessage message = new SendMessage(idChat, textMessage);
        telegramBot.execute(message);
    }
}
