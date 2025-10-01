package app.rolly.backend.repository;

import app.rolly.backend.model.Cathegory;
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
    private CathegoryRepository cathegoryRepository;

    private Cathegory cathegory;
    private Trick trick;
    private Trick savedTrick;

    @BeforeEach
    void set(){
        cathegory = new Cathegory("testCathegory");
        cathegoryRepository.save(cathegory);

        trick = new Trick(
                cathegory,
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
        assertEquals(cathegory, foundTrick.get().getCathegory());
        assertEquals("testTrickName", foundTrick.get().getName());
        assertEquals("http://video.com", foundTrick.get().getVideoLink());
        assertEquals("test description", foundTrick.get().getDescription());
    }
}
