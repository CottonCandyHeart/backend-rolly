package app.rolly.backend.repository;

import app.rolly.backend.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LocationRepositoryUnitTest {
    @Autowired
    private LocationRepository locationRepository;

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
}
