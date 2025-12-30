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
        userRepository.save(user);

        return true;
    }

    public boolean markAsRead(Long id){
        Optional<Notification> notification = notificationRepository.findById(id);

        if (notification.isEmpty()) return false;
        if (notification.get().isRead()) return false;

        notification.get().setRead(true);
        notificationRepository.save(notification.get());

        return true;
    }

    public boolean removeNotification(NotificationDto notificationDto, String username){
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Notification> notification = notificationRepository.findByTitleAndMessageAndSentAtAndReadAndRecipient(
                notificationDto.getTitle(),
                notificationDto.getMessage(),
                notificationDto.getSentAt(),
                notificationDto.isRead(),
                user.get()
        );

        if (notification.isEmpty()) return false;
        notificationRepository.removeById(notification.get().getId());

        return true;
    }
}
