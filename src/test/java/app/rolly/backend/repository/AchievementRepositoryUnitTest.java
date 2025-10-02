package app.rolly.backend.repository;

import app.rolly.backend.model.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AchievementRepositoryUnitTest {
    @Autowired
    private AchievementRepository achievementRepository;

    private Achievement achievement;
    private Achievement savedAchievement;

    @BeforeEach
    void set(){
        achievement = new Achievement("testName", "lorem ipsum", "testPath");
        savedAchievement = achievementRepository.save(achievement);
    }

    @Test
    void shouldSaveAchievement(){
        // Given

        // When

        // Then
        assertNotNull(savedAchievement.getId());
    }

    @Test
    void shouldFindAchievement(){
        // Given

        // When
        Optional<Achievement> foundAchievement = achievementRepository.findById(savedAchievement.getId());

        // Then
        assertTrue(foundAchievement.isPresent());
        assertEquals("testName", foundAchievement.get().getName());
        assertEquals("lorem ipsum", foundAchievement.get().getDescription());
        assertEquals("testPath", foundAchievement.get().getPicturePath());
    }
}
