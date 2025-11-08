package app.rolly.backend.repository;

import app.rolly.backend.model.Achievement;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AchievementRepositoryUnitTest {
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Achievement achievement;
    private Achievement savedAchievement;

    @BeforeEach
    void set(){
        achievement = new Achievement("testName", "testType", "lorem ipsum", "testPath", 0.0, 0, 0, 0);
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

    @Test
    void shouldReturnNullForNonExistingAchievement(){
        // Given

        // When
        Optional<Achievement> foundAchievement = achievementRepository.findById(-1L);

        // Then
        assertTrue(foundAchievement.isEmpty());
    }

    @Test
    void shouldFindAttachedUsers(){
        // Given
        Role role = new Role("testRole", "lorem ipsum");
        roleRepository.save(role);
        User user1 = new User(
                "test1",
                "test1@test1",
                "test1Passwd",
                LocalDate.of(2001, 1, 1),
                role
        );
        User user2 = new User(
                "test2",
                "test2@test2",
                "test2Passwd",
                LocalDate.of(2002, 2, 2),
                role
        );

        user1.getAchievements().add(achievement);
        user2.getAchievements().add(achievement);
        achievement.getUsers().add(user1);
        achievement.getUsers().add(user2);

        userRepository.save(user1);
        userRepository.save(user2);
        achievementRepository.save(achievement);
        achievementRepository.save(achievement);

        // When
        Optional<Achievement> foundAchievement = achievementRepository.findById(achievement.getId());
        Set<User> users = foundAchievement.get().getUsers();

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
        Optional<Achievement> foundAchievement = achievementRepository.findById(achievement.getId());
        Set<User> users = foundAchievement.get().getUsers();

        // Then
        assertTrue(users.isEmpty());
    }
}
