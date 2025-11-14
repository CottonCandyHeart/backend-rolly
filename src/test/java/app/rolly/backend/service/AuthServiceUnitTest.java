package app.rolly.backend.service;

import app.rolly.backend.dto.UserDto;
import app.rolly.backend.exception.UserAlreadyExistsException;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldThrowExceptionForExistingUser(){
        // Given
        User user = new User("username", "email", "passwd",
                LocalDate.of(2000,1,1), new Role("role", "role"));

        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPasswd("passwd");
        userDto.setEmail("email");
        userDto.setBirthday(LocalDate.of(2000,1,1));
        userDto.setRole("role");

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(UserAlreadyExistsException.class, ()->{
            authService.registerUser(userDto);
        });
    }
}
