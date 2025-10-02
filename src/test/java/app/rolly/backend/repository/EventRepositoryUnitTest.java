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
                "testCity",
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

}
