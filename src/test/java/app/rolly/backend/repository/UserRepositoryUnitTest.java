package app.rolly.backend.repository;

import app.rolly.backend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryUnitTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private RouteRepository routeRepository;

    private Role role;
    private LocalDate date;
    private User user;
    private User savedUser;

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
        savedUser = userRepository.save(user);
    }

    @Test
    void shouldSaveUser(){
        // Given

        // When

        // Then
        assertNotNull(savedUser.getId());
    }

    @Test
    void shouldFindUser(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("testUsername", foundUser.get().getUsername());
        assertEquals("test@test", foundUser.get().getEmail());
        assertEquals("testPassword", foundUser.get().getHashedPasswd());
        assertEquals(date, foundUser.get().getDateOfBirth());
        assertEquals(role, foundUser.get().getRole());
    }

    @Test
    void shouldReturnNullForNonExistingUser(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(-1L);

        // Then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldFindAttachedEvents(){
        // Given
        Location location = new Location(
                "testName",
                "testCity",
                "testCountry",
                0.0,
                1.1
        );
        locationRepository.save(location);

        Event event1 = new Event(
                "event1",
                "event1 desc",
                user,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(12,20,0),
                "test1Level",
                "test1Type",
                "test1Age",
                5,
                location
        );
        Event event2 = new Event(
                "event2",
                "event2 desc",
                user,
                LocalDate.of(2026, 2, 2),
                LocalTime.of(10,10,10),
                "test2Level",
                "test2Type",
                "test2Age",
                15,
                location
        );

        user.getOrganizedEvents().add(event1);
        user.getOrganizedEvents().add(event2);

        eventRepository.save(event1);
        eventRepository.save(event2);
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        List<Event> organizedEvents = foundUser.get().getOrganizedEvents();

        // Then
        assertFalse(organizedEvents.isEmpty());
        assertEquals(2, organizedEvents.size());
        assertTrue(organizedEvents.contains(event1));
        assertTrue(organizedEvents.contains(event2));
    }

    @Test
    void shouldReturnEmptyEventList(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        List<Event> organizedEvents = foundUser.get().getOrganizedEvents();

        // Then
        assertTrue(organizedEvents.isEmpty());
    }

    @Test
    void shouldFindAttachedAchievements(){
        // Given
        Achievement achievement1 = new Achievement("test1Name", "testType", "lorem ipsum 1", "test1Path", 0.0, 0, 0, 0);
        Achievement achievement2 = new Achievement("test2Name", "testType", "lorem ipsum 2", "test2Path", 0.0, 0, 0, 0);

        user.getAchievements().add(achievement1);
        user.getAchievements().add(achievement2);
        achievement1.getUsers().add(user);
        achievement2.getUsers().add(user);

        achievementRepository.save(achievement1);
        achievementRepository.save(achievement2);
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());
        Set<Achievement> achievements = foundUser.get().getAchievements();

        // Then
        assertFalse(achievements.isEmpty());
        assertEquals(2, achievements.size());
        assertTrue(achievements.contains(achievement1));
        assertTrue(achievements.contains(achievement2));
    }

    @Test
    void shouldReturnEmptyAchievementList(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());
        Set<Achievement> achievements = foundUser.get().getAchievements();

        // Then
        assertTrue(achievements.isEmpty());
    }

    @Test
    void shouldFindAttachedNotifications(){
        // Given
        Notification notification1 = new Notification(
                "testTitle",
                "testMessage",
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                user
        );
        Notification notification2 = new Notification(
                "testTitle",
                "testMessage",
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                user
        );

        user.getNotifications().add(notification1);
        user.getNotifications().add(notification2);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());
        List<Notification> notifications = foundUser.get().getNotifications();

        // Then
        assertFalse(notifications.isEmpty());
        assertEquals(2, notifications.size());
        assertTrue(notifications.contains(notification1));
        assertTrue(notifications.contains(notification2));
    }

    @Test
    void shouldReturnEmptyNotificationList(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());
        List<Notification> notifications = foundUser.get().getNotifications();

        // Then
        assertTrue(notifications.isEmpty());
    }

    @Test
    void shouldFindAttachedRoutes(){
        // Given
        Route route1 = new Route(
                "test1Name",
                1.5,
                Duration.ofMinutes(50),
                user,
                150
        );
        Route route2 = new Route(
                "test2Name",
                2.5,
                Duration.ofMinutes(100),
                user,
                250
        );

        user.getRoutesCreated().add(route1);
        user.getRoutesCreated().add(route2);

        routeRepository.save(route1);
        routeRepository.save(route2);
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());
        List<Route> routesCreated = foundUser.get().getRoutesCreated();

        // Then
        assertFalse(routesCreated.isEmpty());
        assertEquals(2, routesCreated.size());
        assertTrue(routesCreated.contains(route1));
        assertTrue(routesCreated.contains(route2));
    }

    @Test
    void shouldReturnEmptyRouteList(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());
        List<Route> routesCreated = foundUser.get().getRoutesCreated();

        // Then
        assertTrue(routesCreated.isEmpty());
    }
}
