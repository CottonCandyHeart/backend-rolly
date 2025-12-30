package app.rolly.backend.repository;

import app.rolly.backend.model.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CityRepositoryUnitTest {
    @Autowired
    private CityRepository cityRepository;

    private City city;
    private City savedCity;

    @BeforeEach
    void set(){
        city = new City("city");
        savedCity = cityRepository.save(city);
    }

    @Test
    void shouldSaveCity(){
        // Given

        // When

        // Then
        assertNotNull(savedCity.getId());
    }

    @Test
    void shouldFindCity(){
        // Given

        // When
        Optional<City> result = cityRepository.findById(city.getId());
        boolean result2 = cityRepository.existsByCity("city");

        // Then
        assertTrue(result.isPresent());
        assertTrue(result2);
        assertEquals("city", result.get().getCity());
    }

    @Test
    void shouldReturnNullForNonExistingCity(){
        // Given

        // When
        Optional<City> result = cityRepository.findById(-1L);
        boolean result2 = cityRepository.existsByCity("wrongCity");

        // Then
        assertTrue(result.isEmpty());
        assertFalse(result2);
    }
}
