package app.rolly.backend.repository;

import app.rolly.backend.model.Role;
import app.rolly.backend.model.TrainingPlan;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TrainingPlanRepositoryUnitTest {
    @Autowired
    private TrainingPlanRepository trainingPlanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private TrainingPlan trainingPlan;

    @BeforeEach
    void set(){
        Role role = new Role("role", "role");
        roleRepository.save(role);

        User user = new User(
                "username",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                role
        );
        userRepository.save(user);

        trainingPlan = new TrainingPlan(
                LocalDateTime.of(2025,1,1,1,1,1),
                60,
                "none",
                false,
                user
        );

        trainingPlanRepository.save(trainingPlan);
    }

    @Test
    void shouldSaveTrainingPlan(){
        // Given

        // When

        // Then
        assertNotNull(trainingPlanRepository.findById(trainingPlan.getId()));
    }

    @Test
    void shouldFindSavedTrainingPlan(){
        // Given

        // When
        Optional<TrainingPlan> result = trainingPlanRepository.findById(trainingPlan.getId());

        // Then
        assertEquals(LocalDateTime.of(2025,1,1,1,1,1), result.get().getDateTime());
        assertEquals(60, result.get().getTargetDuration());
        assertEquals("none", result.get().getNotes());
        assertFalse(result.get().isCompleted());
        assertEquals("username", result.get().getUser().getUsername());
    }

    @Test
    void shouldReturnNullForNonExistingTrainingPlan(){
        // Given

        // When
        Optional<TrainingPlan> result = trainingPlanRepository.findById(-1L);

        // Then
        assertTrue(result.isEmpty());
    }
}
