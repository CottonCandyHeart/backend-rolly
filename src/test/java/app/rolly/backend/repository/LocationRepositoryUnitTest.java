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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LocationRepositoryUnitTest {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    private Location location;
    private Location savedLocation;

    @BeforeEach
    void set(){
        location = new Location(
                "testName",
                "testCity",
                "testCountry",
                0.0,
                0.0
        );
        savedLocation = locationRepository.save(location);
    }

    @Test
    void shouldSaveLocation(){
        // Given

        // When

        // Then
        assertNotNull(savedLocation.getId());
    }

    @Test
    void shouldFindLocation(){
        // Given

        // When
        Optional<Location> foundLocation = locationRepository.findById(savedLocation.getId());

        // Then
        assertTrue(foundLocation.isPresent());
        assertEquals("testName", foundLocation.get().getName());
        assertEquals("testCity", foundLocation.get().getCity());
        assertEquals("testCountry", foundLocation.get().getCountry());
        assertEquals(0.0, foundLocation.get().getLatitude());
        assertEquals(0.0, foundLocation.get().getLongitude());
    }

    @Test
    void shouldReturnNullForNonExistingLocation(){
        // Given

        // When
        Optional<Location> foundLocation = locationRepository.findById(-1L);

        // Then
        assertTrue(foundLocation.isEmpty());
    }

    @Test
    void shouldFindAttachedEvents(){
        // Given
        Role role = new Role("testRole", "lorem ipsum");
        roleRepository.save(role);
        LocalDate date = LocalDate.of(2000, 1, 1);
        User user = new User(
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
                10,
                location
        );

        location.getEvents().add(event1);
        location.getEvents().add(event2);

        eventRepository.save(event1);
        eventRepository.save(event2);
        locationRepository.save(location);

        // When
        Optional<Location> foundLocation = locationRepository.findById(location.getId());
        List<Event> events = foundLocation.get().getEvents();

        // Then
        assertFalse(events.isEmpty());
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }

    @Test
    void shouldReturnNullEventSet(){
        // Given

        // When
        Optional<Location> foundLocation = locationRepository.findById(location.getId());
        List<Event> events = foundLocation.get().getEvents();

        // Then
        assertTrue(events.isEmpty());
    }
}
