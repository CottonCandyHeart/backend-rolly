package app.rolly.backend.service;

import app.rolly.backend.dto.AchievementDto;
import app.rolly.backend.model.Achievement;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.AchievementRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceUnitTest {
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AchievementService achievementService;

    private Achievement a1;
    private Achievement a2;
    private Achievement a3;
    private Achievement a4;

    private User user1;
    private User user2;

    @BeforeEach
    void set(){
        a1 = new Achievement("name1", "type1", "lorem ipsum 1", "path1",
                5.5, 0, 0, 0);

        a2 = new Achievement("name2", "type2", "lorem ipsum 2", "path2",
                0.0, 5, 0, 0);

        a3 = new Achievement("name3", "type3", "lorem ipsum 3", "path3",
                0.0, 0, 5, 0);

        a4 = new Achievement("name4", "type4", "lorem ipsum 4", "path4",
                0.0, 0, 0, 5);

        user1 = new User(
                "username1", "email1", "hashedPasswd1",
                LocalDate.of(2000,1,1), new Role("role", "role"));
        user2 = new User(
                "username2", "email2", "hashedPasswd2",
                LocalDate.of(2000,1,1), new Role("role", "role"));

        user1.getAchievements().add(a1);
        user1.getAchievements().add(a2);
        user1.getAchievements().add(a3);
        user2.getAchievements().add(a4);

        a1.getUsers().add(user1);
        a2.getUsers().add(user1);
        a3.getUsers().add(user1);
        a4.getUsers().add(user2);
    }

    @Test
    void shouldReturnAchievementDtoSetForUser(){
        // Given
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));

        // When
        Set<AchievementDto> result1 = achievementService.getUserAchievements("username1");
        Set<AchievementDto> result2 = achievementService.getUserAchievements("username2");

        // Then
        assertEquals(3, result1.size());
        assertEquals(1, result2.size());
    }

    @Test
    void shouldReturnEmptyAchievementSetForUser(){
        // Given
        User user3 = new User(
                "username3", "email3", "hashedPasswd3",
                LocalDate.of(2000,1,1), new Role("role", "role"));
        when(userRepository.findByUsername(user3.getUsername())).thenReturn(Optional.of(user3));

        // When
        Set<AchievementDto> result = achievementService.getUserAchievements("username3");

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllAchievementDtoSet(){
        // Given
        when(achievementRepository.findAll()).thenReturn(List.of(a1,a2,a3,a4));

        // When
        Set<AchievementDto> result = achievementService.getAllAchievements();

        // Then
        assertEquals(4, result.size());
    }

    @Test
    void shouldReturnEmptyAchievementDtoSet(){
        // Given

        // When
        Set<AchievementDto> result = achievementService.getAllAchievements();

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void shouldAddAchievementToUser(){
        // Given
        User user3 = new User(
                "username3", "email3", "hashedPasswd3",
                LocalDate.of(2000,1,1), new Role("role", "role"));

        when(achievementRepository.findById(a1.getId())).thenReturn(Optional.of(a1));
        when(userRepository.findByUsername(user3.getUsername())).thenReturn(Optional.of(user3));

        // When
        boolean result = achievementService.addAchievementToUser("username3", a1.getId());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserAlreadyHasAchievement(){
        // Given
        User user3 = new User(
                "username3", "email3", "hashedPasswd3",
                LocalDate.of(2000,1,1), new Role("role", "role"));
        user3.getAchievements().add(a1);

        when(achievementRepository.findById(a1.getId())).thenReturn(Optional.of(a1));
        when(userRepository.findByUsername(user3.getUsername())).thenReturn(Optional.of(user3));

        // When
        boolean result = achievementService.addAchievementToUser("username3", a1.getId());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenAchievementToAddNotFound(){
        // Given
        when(achievementRepository.findById(a1.getId())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));

        // When
        // Then
        assertThrows(RuntimeException.class,()->{
            achievementService.addAchievementToUser("username2", a1.getId());
        });
    }

    @Test
    void shouldRemoveAchievementFromUser(){
        // Given
        when(achievementRepository.findById(a1.getId())).thenReturn(Optional.of(a1));
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        // When
        boolean result = achievementService.removeAchievementFromUser("username1", a1.getId());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAchievementIsNotOnList(){
        // Given
        when(achievementRepository.findById(a1.getId())).thenReturn(Optional.of(a1));
        when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));

        // When
        boolean result = achievementService.removeAchievementFromUser("username2", a1.getId());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenAchievementToRemoveNotFound(){
        // Given
        when(achievementRepository.findById(a1.getId())).thenReturn(null);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        // When
        // Then
        assertThrows(RuntimeException.class, ()->{
            achievementService.removeAchievementFromUser("username1", a1.getId());
        });
    }
}
