package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskService {

    void addNotification(Long idChat, String text, LocalDateTime date);

    List<NotificationTask> getNotifications(LocalDateTime date);
}
