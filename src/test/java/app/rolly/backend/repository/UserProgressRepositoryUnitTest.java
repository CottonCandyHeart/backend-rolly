package app.rolly.backend.repository;

import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserProgressRepositoryUnitTest {
    @Autowired
    private UserProgressRepository userProgressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private LocalDate date;
    private User user;
    private LocalDateTime lastUpdate;
    private UserProgress userProgress;
    private UserProgress savedUserProgress;

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
        lastUpdate = LocalDateTime.of(2025, 10, 1, 0,0,0);

        userProgress = new UserProgress(
                1.5,
                1,
                0,
                1,
                lastUpdate
        );
        savedUserProgress = userProgressRepository.save(userProgress);

    }

    @Test
    void shouldSaveUserProgress(){
        // Given

        // When

        // Then
        assertNotNull(savedUserProgress.getId());
    }

    @Test
    void shouldFindUserProgress(){
        // Given

        // When
        Optional<UserProgress> foundUserProgress = userProgressRepository.findById(savedUserProgress.getId());

        // Then
        assertTrue(foundUserProgress.isPresent());
        assertEquals(1.5, foundUserProgress.get().getTotalDistance());
        assertEquals(1, foundUserProgress.get().getTotalSessions());
        assertEquals(0, foundUserProgress.get().getTotalTricksLearned());
        assertEquals(1, foundUserProgress.get().getCaloriesBurned());
        assertEquals(lastUpdate, foundUserProgress.get().getLastUpdated());
    }
}
