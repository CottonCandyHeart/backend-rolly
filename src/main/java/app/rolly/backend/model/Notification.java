package app.rolly.backend.model;

import app.rolly.backend.dto.NotificationDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Notification(String title, String message, LocalDateTime sentAt, User recipient){
        this.title = title;
        this.message = message;
        this.sentAt = sentAt;
        this.read = false;
        this.recipient = recipient;
    }

    public Notification(NotificationDto notificationDto, User user){
        this.title = notificationDto.getTitle();
        this.message = notificationDto.getMessage();
        this.sentAt = notificationDto.getSentAt();
        this.read = false;
        this.recipient = user;
    }

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private LocalDateTime sentAt;
    @Column(nullable = false)
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
}
