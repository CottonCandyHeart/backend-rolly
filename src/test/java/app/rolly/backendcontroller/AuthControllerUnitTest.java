package app.rolly.backendcontroller;

import app.rolly.backend.auth.JwtUtils;
import app.rolly.backend.controller.AuthController;
import app.rolly.backend.dto.LoginRequest;
import app.rolly.backend.dto.UserDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthControllerUnitTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AuthService authService;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    public AuthControllerUnitTest(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully(){
        // Given
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPasswd("testPasswd");
        userDto.setEmail("test@test.pl");
        userDto.setYear(2000);
        userDto.setMonth(1);
        userDto.setDay(1);
        userDto.setRole("USER");

        Role role = new Role("USER", "lorem ipsum");
        when(roleRepository.findByName("USER")).thenReturn(role);

        // When
        ResponseEntity<?> response = authController.register(userDto);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User created", response.getBody());
        verify(authService)
                .registerUser(
                        eq("testUser"),
                        eq("test@test.pl"),
                        eq("testPasswd"),
                        eq(LocalDate.of(2000,1,1)),
                        eq(role)
                );
    }

    @Test
    void shouldFailRegistrationWhenUsernameExists(){
        // Given
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPasswd("testPasswd");
        userDto.setEmail("test@test.pl");
        userDto.setYear(2000);
        userDto.setMonth(1);
        userDto.setDay(1);
        userDto.setRole("USER");

        Role role = new Role("USER", "lorem ipsum");
        when(roleRepository.findByName("USER")).thenReturn(role);

        doThrow(new IllegalArgumentException("Username already exists"))
                .when(authService)
                .registerUser(
                        eq("testUser"),
                        eq("test@test.pl"),
                        eq("testPasswd"),
                        eq(LocalDate.of(2000,1,1)),
                        eq(role)
                );

        // When
        ResponseEntity<?> response = authController.register(userDto);

        // Then
        assertEquals(422, response.getStatusCode().value());
        assertEquals(
                "Cannot create user: java.lang.IllegalArgumentException: Username already exists",
                response.getBody()
        );
    }

    @Test
    void shouldLoginUserSuccessfully(){
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPasswd("testPasswd");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(auth);

        when(jwtUtils.generateJwtToken(auth)).thenReturn("mocked-jwt-token");

        // When
        ResponseEntity<?> response = authController.login(loginRequest);

        // Then
        assertEquals(200, response.getStatusCode().value());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("mocked-jwt-token", body.get("token"));
    }

    @Test
    void shouldFailUserLoginWhenPasswordIsIncorrect(){
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPasswd("testPasswd");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        // When
        ResponseEntity<?> response = authController.login(loginRequest);

        // Then
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid username or password", response.getBody());
    }
}
