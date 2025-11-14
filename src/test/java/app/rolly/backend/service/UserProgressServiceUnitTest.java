package app.rolly.backend.service;

import app.rolly.backend.dto.UserProgressDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserProgressServiceUnitTest {
    @Mock
    private UserProgressRepository userProgressRepository;

    @InjectMocks
    private UserProgressService userProgressService;

    private UserProgress userProgress;
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

        userProgress = new UserProgress(
                1.1,
                2,
                3,
                4,
                LocalDateTime.of(2025,1,1,1,1,1),
                user
        );

        user.setUserProgress(userProgress);
    }

    @Test
    void shouldReturnUserProgressDto(){
        // Given
        UserProgressDto expected = new UserProgressDto(userProgress);

        // When
        UserProgressDto result = userProgressService.getUserProgress(user.getUsername());

        // Then
        assertEquals(expected.getUsername(), result.getUsername());
        assertEquals(expected.getTotalSessions(), result.getTotalSessions());
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getLastUpdated(), result.getLastUpdated());
        assertEquals(expected.getTotalTricksLearned(), result.getTotalTricksLearned());
        assertEquals(expected.getCaloriesBurned(), result.getCaloriesBurned());
        assertEquals(expected.getTotalDistance(), result.getTotalDistance());
    }

    @Test
    void shouldUpdateUserProgress(){
        // Given
        UserProgress userProgress2 = new UserProgress(
                5.5,
                6,
                7,
                8,
                LocalDateTime.of(2025,2,2,2,2,2),
                user
        );

        UserProgressDto userProgressDto = new UserProgressDto(userProgress2);

        // When
        userProgressService.updateStats(userProgressDto, user.getUsername());
        UserProgress result = user.getUserProgress();

        // Then
        assertEquals(5.5, result.getTotalDistance());
        assertEquals(6, result.getTotalSessions());
        assertEquals(7, result.getTotalTricksLearned());
        assertEquals(8, result.getCaloriesBurned());
        assertEquals(userProgress2.getLastUpdated(), result.getLastUpdated());
        assertEquals(user, result.getUser());
    }
}
