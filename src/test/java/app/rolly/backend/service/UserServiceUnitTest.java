package app.rolly.backend.service;

import app.rolly.backend.dto.*;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        assertThrows(IllegalArgumentException.class,() -> {
            userService.changePassword(request, user);
        } );
    }
}
