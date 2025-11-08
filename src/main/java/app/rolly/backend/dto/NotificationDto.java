package app.rolly.backend.dto;

import app.rolly.backend.model.Notification;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private LocalDateTime sentAt;
    private boolean read;
    private String username;

    public NotificationDto(Notification notification){
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.sentAt = notification.getSentAt();
        this.read = notification.isRead();
        this.username = notification.getRecipient().getUsername();
    }
}
