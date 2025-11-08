package app.rolly.backend.repository;

import app.rolly.backend.model.Notification;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NotificationRepositoryUnitTest {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private LocalDate date;
    private User user;
    private Notification notification;
    private Notification savedNotification;
    private LocalDateTime sentAt;

    @BeforeEach
    void set(){
        role = new Role("testRole", "lorem ipsum");
        roleRepository.save(role);

        date = LocalDate.of(2000, 1, 1);

        user = new User(
                "testUsername",
                "test@test",
                "testPassword",
                date,
                role
        );
        userRepository.save(user);

        sentAt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        notification = new Notification(
                "testTitle",
                "testMessage",
                sentAt,
                user
        );
        savedNotification = notificationRepository.save(notification);
    }

    @Test
    void shouldSaveNotification(){
        // Given

        // When

        // Then
        assertNotNull(savedNotification.getId());
    }

    @Test
    void shouldFindNotification(){
        // Given

        // When
        Optional<Notification> foundNotification = notificationRepository.findById(savedNotification.getId());

        // Then
        assertTrue(foundNotification.isPresent());
        assertEquals("testTitle", foundNotification.get().getTitle());
        assertEquals("testMessage", foundNotification.get().getMessage());
        assertEquals(sentAt, foundNotification.get().getSentAt());
        assertFalse(foundNotification.get().isRead());
        assertEquals(user, foundNotification.get().getRecipient());
    }

    @Test
    void shouldReturnNullForNonExistingNotification(){
        // Given

        // When
        Optional<Notification> foundNotification = notificationRepository.findById(-1L);

        // Then
        assertTrue(foundNotification.isEmpty());
    }
}
