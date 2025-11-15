package app.rolly.backend.repository;

import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EventRepositoryUnitTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Location location;
    private Event event;
    private Event savedEvent;
    private LocalDate eventDate;
    private LocalDate date;
    private LocalTime time;
    private Role role;

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
        location = new Location(
                "testName",
                "testCity",
                "testCountry",
                0.0,
                1.1
        );
        locationRepository.save(location);

        eventDate = LocalDate.of(2025, 1, 1);
        time = LocalTime.of(12,20,0);
        event = new Event(
                user,
                eventDate,
                time,
                "testLevel",
                "testType",
                "testAge",
                5,
                location
        );
        savedEvent = eventRepository.save(event);
    }

    @Test
    void shouldSaveEvent(){
        // Given

        // When

        // Then
        assertNotNull(savedEvent.getId());
    }

    @Test
    void shouldFindEvent(){
        // Given

        // When
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());

        // Then
        assertTrue(foundEvent.isPresent());
        assertEquals(user, foundEvent.get().getOrganizer());
        assertEquals(eventDate, foundEvent.get().getDate());
        assertEquals(time, foundEvent.get().getTime());
        assertEquals("testCity", foundEvent.get().getCity());
        assertEquals("testLevel", foundEvent.get().getLevel());
        assertEquals("testType", foundEvent.get().getType());
        assertEquals("testAge", foundEvent.get().getAge());
        assertEquals(5, foundEvent.get().getNumOfParticipants());
        assertEquals(location, foundEvent.get().getLocation());
    }

    @Test
    void shouldReturnNullForNonExistingEvent(){
        // Given

        // When
        Optional<Event> foundEvent = eventRepository.findById(-1L);

        // Then
        assertTrue(foundEvent.isEmpty());
    }

    @Test
    void shouldFindParticipants(){
        // Given
        LocalDate date1 = LocalDate.of(2002, 2, 2);
        LocalDate date2 = LocalDate.of(2003, 3, 3);
        User user1 = new User(
                "test1",
                "test1@test1",
                "test1Password",
                date1,
                role
        );
        User user2 = new User(
                "test2",
                "test2@test2",
                "test2Password",
                date2,
                role
        );
        event.getAttendee().add(user1);
        event.getAttendee().add(user2);

        userRepository.save(user1);
        userRepository.save(user2);
        eventRepository.save(event);

        // When
        Optional<Event> foundEvent = eventRepository.findById(event.getId());
        Set<User> users = foundEvent.get().getAttendee();

        // Then
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void shouldReturnEmptyUserSet(){
        // Given

        // When
        Optional<Event> foundEvent = eventRepository.findById(event.getId());
        Set<User> users = foundEvent.get().getAttendee();

        // Then
        assertTrue(users.isEmpty());
    }
}
