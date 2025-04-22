package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM notification_task where date_notification = ?1")
    List<NotificationTask> findAllByDateTimeEquals(LocalDateTime dateTime);
}
