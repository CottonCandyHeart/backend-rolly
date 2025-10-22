package app.rolly.backend.repository;

import app.rolly.backend.model.Category;
import app.rolly.backend.model.Trick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TrickRepositoryUnitTest {
    @Autowired
    private TrickRepository trickRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Trick trick;
    private Trick savedTrick;

    @BeforeEach
    void set(){
        category = new Category("testCathegory");
        categoryRepository.save(category);

        trick = new Trick(
                category,
                "testTrickName",
                "http://video.com",
                "test description"
        );
        savedTrick = trickRepository.save(trick);
    }

    @Test
    void shouldSaveTrick(){
        // Given

        // When

        // Then
        assertNotNull(savedTrick.getId());
    }

    @Test
    void shouldFindTrick(){
        // Given

        // When
        Optional<Trick> foundTrick = trickRepository.findById(savedTrick.getId());

        // Then
        assertTrue(foundTrick.isPresent());
        assertEquals(category, foundTrick.get().getCategory());
        assertEquals("testTrickName", foundTrick.get().getName());
        assertEquals("http://video.com", foundTrick.get().getLink());
        assertEquals("test description", foundTrick.get().getDescription());
    }

    @Test
    void shouldReturnNullForNonExistingTrick(){
        // Given

        // When
        Optional<Trick> foundTrick = trickRepository.findById(-1L);

        // Then
        assertTrue(foundTrick.isEmpty());
    }
}
