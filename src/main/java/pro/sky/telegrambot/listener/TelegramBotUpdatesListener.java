package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;
import pro.sky.telegrambot.service.SenderMessageService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskService service;
    private final SenderMessageService sender;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService service, SenderMessageService sender) {
        this.telegramBot = telegramBot;
        this.service = service;
        this.sender = sender;
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
                sender.sendMessage(update.message().chat().id(), "Приветствую тебя, друг, в Планировщик-Боте!");
            } else if (matcher.matches()) {
                String date = matcher.group(1);
                String textNotification = matcher.group(3);

                LocalDateTime dateNotification = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                LocalDateTime dateCurrent = LocalDateTime.now();

                if (dateNotification.isBefore(dateCurrent)) {
                    sender.sendMessage(update.message().chat().id(), "Друг, ты указал дату меньше текущей! Я не могу сохранить твою напоминалку :(");
                } else {
                    service.addNotification(update.message().chat().id(), textNotification, dateNotification);
                    sender.sendMessage(update.message().chat().id(), "Твоя напоминалка сохранена!");
                }
            } else {
                sender.sendMessage(update.message().chat().id(), "Друг, твоя напоминалка не соответствует заданному формату.");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
