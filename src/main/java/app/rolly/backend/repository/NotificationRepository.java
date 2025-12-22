package app.rolly.backend.repository;

import app.rolly.backend.model.Notification;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByRecipient(User recipient);

    void removeById(Long id);

    Optional<Notification> findByTitleAndMessageAndSentAtAndRead(String title, String message, LocalDateTime sentAt, boolean read);

    Optional<Notification> findByTitleAndMessageAndSentAtAndReadAndRecipient(String title, String message, LocalDateTime sentAt, boolean read, User recipient);
}
