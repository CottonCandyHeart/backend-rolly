package app.rolly.backend.repository;

import app.rolly.backend.model.Notification;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByRecipient(User recipient);

    void removeById(Long id);
}
