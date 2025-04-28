package pro.sky.telegrambot.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SchedulerService {

    private final NotificationTaskService service;
    private final SenderMessageService sender;

    public SchedulerService(NotificationTaskService service, SenderMessageService sender) {
        this.service = service;
        this.sender = sender;
    }

    // метод по отправке напоминалок в указанное время
    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification() {
        LocalDateTime dateCurrent = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> allNotification = service.getNotifications(dateCurrent);
        allNotification.forEach(notificationTask ->
        {
            String message = "Друг, у тебя запланировано - " + notificationTask.getTextNotification();
            sender.sendMessage(notificationTask.getIdChat(), message);
        });
    }
}
