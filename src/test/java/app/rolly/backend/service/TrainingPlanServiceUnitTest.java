package app.rolly.backend.service;

import app.rolly.backend.dto.TrainingPlanDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.TrainingPlan;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.TrainingPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingPlanServiceUnitTest {
    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @InjectMocks
    private TrainingPlanService trainingPlanService;

    private TrainingPlan trainingPlan;
    private User user;

    @BeforeEach
    void set(){
        user = new User(
                "username",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );

        trainingPlan = new TrainingPlan(
                LocalDateTime.of(2025,1,1,1,1,1),
                60,
                "none",
                false,
                user
        );
    }

    @Test
    void shouldReturnListOfTrainingPlanDtosForUser(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,1,1,1,1,1),
                60,
                "none",
                false,
                user
        );
        when(trainingPlanRepository.findByUser(user)).thenReturn(List.of(trainingPlan, trainingPlan2));

        // When
        List<TrainingPlanDto> result = trainingPlanService.getTrainingPlans(user);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyTrainingPlanListForUser(){
        // Given
        when(trainingPlanRepository.findByUser(user)).thenReturn(null);

        // When
        List<TrainingPlanDto> result = trainingPlanService.getTrainingPlans(user);

        // Then
        assertNull(result);
    }

    @Test
    void shouldAddTrainingPlanSuccessfully(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,2,1,1,1,1),
                60,
                "none",
                false,
                user
        );
        TrainingPlanDto trainingPlanDto = new TrainingPlanDto(trainingPlan2);

        // When
        boolean result = trainingPlanService.addTrainingPlan(trainingPlanDto, user);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDateIsAlreadyInUse(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,1,1,1,1,1),
                60,
                "none",
                false,
                user
        );
        TrainingPlanDto trainingPlanDto = new TrainingPlanDto(trainingPlan2);

        when(trainingPlanRepository.existsByDateTimeAndUser(LocalDateTime.of(2025,1,1,1,1,1), user))
                .thenReturn(true);

        // When
        boolean result = trainingPlanService.addTrainingPlan(trainingPlanDto, user);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldModifyTrainingPlanSuccessfullyWhenDateIsNotChanged(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,1,1,1,1,1),
                60,
                "new note",
                false,
                user
        );
        TrainingPlanDto trainingPlanDto = new TrainingPlanDto(trainingPlan2);
        trainingPlanDto.setId(trainingPlan.getId());

        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));

        // When
        boolean result = trainingPlanService.modifyTrainingPlan(trainingPlanDto, user);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldModifyTrainingPlanSuccessfullyWhenDateIsChanged(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,2,1,1,1,1),
                60,
                "new note",
                false,
                user
        );
        TrainingPlanDto trainingPlanDto = new TrainingPlanDto(trainingPlan2);
        trainingPlanDto.setId(trainingPlan.getId());

        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));
        when(trainingPlanRepository.existsByDateTimeAndUser(LocalDateTime.of(2025,2,1,1,1,1), user))
                .thenReturn(false);

        // When
        boolean result = trainingPlanService.modifyTrainingPlan(trainingPlanDto, user);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForModifyingWhenTrainingPlanDoesNotExist(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,2,1,1,1,1),
                60,
                "new note",
                false,
                user
        );
        TrainingPlanDto trainingPlanDto = new TrainingPlanDto(trainingPlan2);
        trainingPlanDto.setId(trainingPlan.getId());

        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.empty());

        // When
        boolean result = trainingPlanService.modifyTrainingPlan(trainingPlanDto, user);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForModifyingWhenNewDateIsAlreadyTaken(){
        // Given
        TrainingPlan trainingPlan2 = new TrainingPlan(
                LocalDateTime.of(2025,2,1,1,1,1),
                60,
                "new note",
                false,
                user
        );
        TrainingPlanDto trainingPlanDto = new TrainingPlanDto(trainingPlan2);
        trainingPlanDto.setId(trainingPlan.getId());

        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));
        when(trainingPlanRepository.existsByDateTimeAndUser(LocalDateTime.of(2025,2,1,1,1,1), user))
                .thenReturn(true);

        // When
        boolean result = trainingPlanService.modifyTrainingPlan(trainingPlanDto, user);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldRemoveTrainingPlanSuccessfully(){
        // Given
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));

        // When
        boolean result = trainingPlanService.removeTrainingPlan(trainingPlan.getId());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseFoRemovingNonExistingTrainingPlan(){
        // Given
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.empty());

        // When
        boolean result = trainingPlanService.removeTrainingPlan(trainingPlan.getId());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldSetCompletedTrueWhenIsFalse(){
        // Given
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));

        // When
        boolean result = trainingPlanService.markAsCompleted(trainingPlan.getId(), true);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldSetCompletedFalseWhenIsTrue(){
        // Given
        trainingPlan.setCompleted(true);
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));

        // When
        boolean result = trainingPlanService.markAsCompleted(trainingPlan.getId(), false);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldSReturnFalseWhenCompletedIsAlreadyTrue(){
        // Given
        trainingPlan.setCompleted(true);
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));

        // When
        boolean result = trainingPlanService.markAsCompleted(trainingPlan.getId(), true);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldSReturnFalseWhenCompletedIsAlreadyFalse(){
        // Given
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.of(trainingPlan));

        // When
        boolean result = trainingPlanService.markAsCompleted(trainingPlan.getId(), false);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForMarkingNonExistingTrainingPlan(){
        // Given
        when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(Optional.empty());

        // When
        boolean result = trainingPlanService.markAsCompleted(trainingPlan.getId(), false);

        // Then
        assertFalse(result);
    }

}
