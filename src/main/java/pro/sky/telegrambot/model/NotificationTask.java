package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idChat;
    private String textNotification;
    private LocalDateTime dateNotification;

    public NotificationTask() {
    }

    public NotificationTask(Long idChat, String textNotification, LocalDateTime dateNotification) {
        this.idChat = idChat;
        this.textNotification = textNotification;
        this.dateNotification = dateNotification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getTextNotification() {
        return textNotification;
    }

    public void setTextNotification(String textNotification) {
        this.textNotification = textNotification;
    }

    public LocalDateTime getDataNotification() {
        return dateNotification;
    }

    public void setDataNotification(LocalDateTime dataNotification) {
        this.dateNotification = dataNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(idChat, that.idChat) && Objects.equals(textNotification, that.textNotification) && Objects.equals(dateNotification, that.dateNotification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idChat, textNotification, dateNotification);
    }
}
