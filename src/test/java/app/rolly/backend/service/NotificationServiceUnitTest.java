package app.rolly.backend.service;

import app.rolly.backend.dto.NotificationDto;
import app.rolly.backend.model.Notification;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.NotificationRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUnitTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
    private Notification n1;

    @BeforeEach
    void set(){
        user = new User("username", "email", "hashedPasswd",
                LocalDate.of(2000,1,1), new Role("role", "lorem ipsum"));

        n1 = new Notification("title", "message",
                LocalDateTime.of(2025,1,1,1,1,1), user);

        user.getNotifications().add(n1);
    }

    @Test
    void shouldGetListOfNotificationDtos(){
        // Given
        Notification n2 = new Notification("title", "message",
                LocalDateTime.of(2025,1,1,1,1,1), user);
        user.getNotifications().add(n2);

        when(notificationRepository.getNotificationsByRecipient(user)).thenReturn(List.of(n1, n2));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // When
        List<NotificationDto> result = notificationService.getNotifications("username");

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldAddNotification(){
        // Given
        Notification n2 = new Notification("title", "message",
                LocalDateTime.of(2025,1,1,1,1,1), user);
        NotificationDto notificationDto = new NotificationDto(n2);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // When
        boolean result = notificationService.addNotification(notificationDto, "username");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldMarkAsRead(){
        // Given
        when(notificationRepository.findById(n1.getId())).thenReturn(Optional.of(n1));

        // When
        boolean result = notificationService.markAsRead(n1.getId());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNonExistingNotification(){
        // Given
        when(notificationRepository.findById(n1.getId())).thenReturn(Optional.empty());

        // When
        boolean result = notificationService.markAsRead(n1.getId());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNotificationIsAlreadyRead(){
        // Given
        n1.setRead(true);
        when(notificationRepository.findById(n1.getId())).thenReturn(Optional.of(n1));

        // When
        boolean result = notificationService.markAsRead(n1.getId());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForRemovingNonExistingNotification(){
        // Given
        NotificationDto notificationDto = new NotificationDto(n1);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(notificationRepository.findByTitleAndMessageAndSentAtAndReadAndRecipient(
                n1.getTitle(),
                n1.getMessage(),
                n1.getSentAt(),
                n1.isRead(),
                user
        )).thenReturn(Optional.empty());

        // When
        boolean result = notificationService.removeNotification(notificationDto, user.getUsername());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldRemoveNotificationSuccessfully(){
        // Given
        NotificationDto notificationDto = new NotificationDto(n1);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(notificationRepository.findByTitleAndMessageAndSentAtAndReadAndRecipient(
                n1.getTitle(),
                n1.getMessage(),
                n1.getSentAt(),
                n1.isRead(),
                user
        )).thenReturn(Optional.of(n1));

        // When
        boolean result = notificationService.removeNotification(notificationDto, user.getUsername());

        // Then
        assertTrue(result);
    }
}
