package app.rolly.backend.service;

import app.rolly.backend.dto.LocationDto;
import app.rolly.backend.model.Location;
import app.rolly.backend.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationServiceUnitTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    void set(){
        location = new Location(
                "testName",
                "testCity",
                "testCountry",
                0.0,
                1.1
        );
    }

    @Test
    void shouldReturnLocationDto(){
        // Given
        when(locationRepository.findByName("testName")).thenReturn(Optional.of(location));

        // When
        LocationDto locationDto = locationService.getLocation("testName");

        // Then
        assertNotNull(locationDto);
        assertEquals("testName", locationDto.getName());
        assertEquals("testCity", locationDto.getCity());
        assertEquals("testCountry", locationDto.getCountry());
        assertEquals(0.0, locationDto.getLatitude());
        assertEquals(1.1, locationDto.getLongitude());
    }

    @Test
    void shouldReturnEmptyLocationDto(){
        // Given
        when(locationRepository.findByName("wrongName")).thenReturn(Optional.empty());

        // When
        LocationDto locationDto = locationService.getLocation("wrongName");

        // Then
        assertNull(locationDto);
    }

    @Test
    void shouldAddLocationSuccessfully(){
        // Given
        Location newLocation = new Location(
                "newLocation",
                "newCity",
                "newCountry",
                2.2,
                3.3
        );
        LocationDto locationDto = new LocationDto(newLocation);

        when(locationRepository.findByName("newLocation")).thenReturn(Optional.empty());

        // When
        boolean result = locationService.addLocation(locationDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForAddingExistingLocation(){
        // Given
        Location newLocation = new Location(
                "newLocation",
                "newCity",
                "newCountry",
                2.2,
                3.3
        );
        LocationDto locationDto = new LocationDto(newLocation);

        when(locationRepository.findByName("newLocation")).thenReturn(Optional.of(newLocation));

        // When
        boolean result = locationService.addLocation(locationDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldRemoveExistingLocation(){
        // Given
        when(locationRepository.findByName("testName")).thenReturn(Optional.of(location));

        // When
        boolean result = locationService.removeLocation("testName");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForRemovingNonExistingLocation(){
        // Given
        when(locationRepository.findByName("wrongName")).thenReturn(Optional.empty());

        // When
        boolean result = locationService.removeLocation("wrongName");

        // Then
        assertFalse(result);
    }

    @Test
    void shouldModifyLocation(){
        // Given
        Location newLocation = new Location(
                "newLocation",
                "newCity",
                "newCountry",
                2.2,
                3.3
        );
        LocationDto locationDto = new LocationDto(newLocation);

        when(locationRepository.findByName("testName")).thenReturn(Optional.of(location));

        // When
        boolean result = locationService.modifyLocation("testName", locationDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNonExistingLocation(){
        // Given
        Location newLocation = new Location(
                "newLocation",
                "newCity",
                "newCountry",
                2.2,
                3.3
        );
        LocationDto locationDto = new LocationDto(newLocation);

        when(locationRepository.findByName("wrongName")).thenReturn(Optional.empty());

        // When
        boolean result = locationService.modifyLocation("wrongName", locationDto);

        // Then
        assertFalse(result);
    }
}
