package app.rolly.backend.service;

import app.rolly.backend.dto.*;
import app.rolly.backend.exception.WrongPasswordException;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService userService;

    private Role role;
    private User user;

    @BeforeEach
    void set(){
        role = new Role("role1", "role1 description");

        when(passwordEncoder.encode("test")).thenReturn("encodedPassword");

        user = new User(
                "testUsername",
                "test@test",
                passwordEncoder.encode("test"),
                LocalDate.of(2000, 1, 1),
                role
        );
    }

    @Test
    void shouldReturnUserResponseDto(){
        // Given
        List<Event> organizedEvents = new ArrayList<>();
        Set<Event> attendedEvents = new HashSet<>();
        Set<Achievement> achievements = new HashSet<>();
        List<Route> routesCreated = new ArrayList<>();

        Event organizedEvent = new Event();
        organizedEvent.setOrganizer(user);
        organizedEvent.setLocation(new Location());
        organizedEvents.add(organizedEvent);

        Event attendedEvent = new Event();
        attendedEvent.setOrganizer(user);
        attendedEvent.setLocation(new Location());
        attendedEvents.add(attendedEvent);

        achievements.add(new Achievement());

        Route route = new Route();
        route.setCreatedBy(user);
        routesCreated.add(route);

        user.setOrganizedEvents(organizedEvents);
        user.setAchievements(achievements);
        user.setAttendedEvents(attendedEvents);
        user.setRoutesCreated(routesCreated);

        // When
        UserResponseDto response = userService.getUserProfile(user);

        // Then
        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getDateOfBirth(), response.getBirthday());
        assertEquals(user.getRole().getName(), response.getRole());

        assertEquals(user.getOrganizedEvents().size(), response.getOrganizedEvents().size());
        assertEquals(user.getAchievements().size(), response.getAchievements().size());
        assertEquals(user.getAttendedEvents().size(), response.getAttendedEvents().size());
        assertEquals(user.getRoutesCreated().size(), response.getRoutesCreated().size());

        assertNull(response.getUserProgress());
    }

    @Test
    void shouldUpdateEmail(){
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("updated@test");
        updateUserDto.setRole(user.getRole().getName());

        // When
        userService.updateUserProfile(updateUserDto, user);

        // Then
        assertEquals("updated@test", user.getEmail());
        assertEquals(role, user.getRole());
    }

    @Test
    void shouldUpdateRole(){
        // Given
        Role role2 = new Role("role2", "role2 description");
        when(roleRepository.findByName(role2.getName())).thenReturn(role2);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setRole(role2.getName());
        updateUserDto.setEmail(user.getEmail());

        // When
        userService.updateUserProfile(updateUserDto, user);

        // Then
        assertEquals(role2.getName(), user.getRole().getName());
        assertEquals("test@test", user.getEmail());
    }

    @Test
    void shouldChangePasswordSuccessfully(){
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPasswd("test");
        request.setNewPasswd("newPassword");

        when(passwordEncoder.matches("test", user.getHashedPasswd())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // When
        userService.changePassword(request, user);

        // Then
        assertEquals("encodedNewPassword", user.getHashedPasswd());
    }

    @Test
    void shouldFailedIfPasswordIsWrong(){
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPasswd("wrongPassword");
        request.setNewPasswd("newPassword");

        // When
        // Then
        assertThrows(WrongPasswordException.class,() -> {
            userService.changePassword(request, user);
        } );
    }

    @Test
    void shouldRemoveExistingUser(){
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        boolean result = userService.removeUser(user.getId());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNonExistingUser(){
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // When
        boolean result = userService.removeUser(user.getId());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldUpdateUserMeasurements(){
        // Given
        UserMeasurementsDto userMeasurementsDto = new UserMeasurementsDto(50.5, 160);

        // When
        boolean result = userService.updateMeasurements(userMeasurementsDto, user);

        // Then
        assertTrue(result);
        assertEquals(50.5, user.getWeight());
        assertEquals(160, user.getHeight());
    }

    @Test
    void shouldGetMeasurementsDto(){
        // Given
        user.setHeight(160);
        user.setWeight(50.5);

        // When
        UserMeasurementsDto measurementsDto = userService.getMeasurements(user);

        // Then
        assertNotNull(measurementsDto);
        assertEquals(160, measurementsDto.getHeight());
        assertEquals(50.5, measurementsDto.getWeight());
    }

    @Test
    void shouldReturnZeroWhenMeasurementsAreEmpty(){
        // Given

        // When
        UserMeasurementsDto measurementsDto = userService.getMeasurements(user);
        System.out.println(measurementsDto.getHeight());

        // Then
        assertEquals(0.0, measurementsDto.getWeight());
        assertEquals(0, measurementsDto.getHeight());
    }
}
