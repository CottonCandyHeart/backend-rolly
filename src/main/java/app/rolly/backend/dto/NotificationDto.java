package app.rolly.backend.dto;

import app.rolly.backend.model.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private String title;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime sentAt;
    private boolean read;

    public NotificationDto(Notification notification){
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.sentAt = notification.getSentAt();
        this.read = notification.isRead();
    }
}
