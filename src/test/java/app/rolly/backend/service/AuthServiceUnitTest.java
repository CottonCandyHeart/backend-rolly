package app.rolly.backend.service;

import app.rolly.backend.dto.UserDto;
import app.rolly.backend.exception.UserAlreadyExistsException;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void set(){
        user = new User("username", "email", "passwd",
                LocalDate.of(2000,1,1), new Role("role", "role"));

        userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPasswd("passwd");
        userDto.setEmail("email");
        userDto.setBirthday(LocalDate.of(2000,1,1));
        userDto.setRole("role");
    }

    @Test
    void shouldRegisterUserWithExistingRole(){
        // Given
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(roleRepository.findByName("role")).thenReturn(Optional.of(new Role("role", "role")));

        // When
        boolean result = authService.registerUser(userDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldRegisterUserWithNonExistingRole(){
        // Given
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(roleRepository.findByName("role")).thenReturn(Optional.empty());

        // When
        boolean result = authService.registerUser(userDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionForExistingUser(){
        // Given
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(UserAlreadyExistsException.class, ()->{
            authService.registerUser(userDto);
        });
    }
}
