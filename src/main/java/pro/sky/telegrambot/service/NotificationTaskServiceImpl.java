package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationTaskServiceImpl implements NotificationTaskService {

    private final NotificationTaskRepository repository;

    public NotificationTaskServiceImpl(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addNotification(Long idChat, String text, LocalDateTime date) {
        NotificationTask newTask = new NotificationTask(idChat, text, date);
        repository.save(newTask);
    }

    @Override
    public List<NotificationTask> getNotifications(LocalDateTime date) {
        return new ArrayList<>(repository.findAllByDateTimeEquals(date));
    }
}
