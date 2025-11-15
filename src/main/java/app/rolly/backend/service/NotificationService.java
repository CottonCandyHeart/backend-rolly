package app.rolly.backend.service;

import app.rolly.backend.dto.NotificationDto;
import app.rolly.backend.model.Notification;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.NotificationRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // TODO wysyłanie powiadomień użytkownikowi (np. „Twoje wydarzenie zaczyna się za godzinę!”).
    //  aktualizacja statusu (read/unread), dodawanie, usuwanie
    //  Joined event, left event, przypomnienie o evencie, przypomnienie o treningu itp

    public List<NotificationDto> getNotifications(String username){
        User user = userRepository.findByUsername(username).get();
        return notificationRepository.getNotificationsByRecipient(user).stream()
                .map(NotificationDto::new)
                .toList();
    }

    public boolean addNotification(NotificationDto notificationDto, String username){
        User user = userRepository.findByUsername(username).get();
        Notification notification = new Notification(notificationDto, user);
        notificationRepository.save(notification);
        user.getNotifications().add(notification);
        return true;
    }

    public boolean markAsRead(Long id){
        Optional<Notification> notification = notificationRepository.findById(id);

        if (notification.isEmpty()) return false;
        if (notification.get().isRead()) return false;

        notification.get().setRead(true);
        return true;
    }
}
