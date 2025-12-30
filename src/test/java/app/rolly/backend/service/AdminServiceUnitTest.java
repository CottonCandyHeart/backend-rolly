package app.rolly.backend.service;

import app.rolly.backend.dto.ChangeRoleDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.dto.UserDto;
import app.rolly.backend.dto.UserResponseDto;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TrickRepository trickRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void shouldReturnAllUserResponseDtoList(){
        // Given
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        User user2 = new User(
                "user2",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        when(userRepository.findAll()).thenReturn(List.of(user1,user2));

        // When
        List<UserResponseDto> result = adminService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals(user1.getUsername(), result.getFirst().getUsername());
        assertEquals(user2.getUsername(), result.getLast().getUsername());
    }

    @Test
    void shouldReturnFalseForAddingExistingUser(){
        // Given
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        UserDto userDto = new UserDto();
        userDto.setBirthday(user1.getDateOfBirth());
        userDto.setRole(user1.getRole().getName());
        userDto.setEmail(user1.getEmail());
        userDto.setPasswd(user1.getHashedPasswd());
        userDto.setUsername(user1.getUsername());

        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        // When
        boolean result = adminService.addUser(userDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldAddUserSuccessfully(){
        // Given
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        UserDto userDto = new UserDto();
        userDto.setBirthday(user1.getDateOfBirth());
        userDto.setRole(user1.getRole().getName());
        userDto.setEmail(user1.getEmail());
        userDto.setPasswd(user1.getHashedPasswd());
        userDto.setUsername(user1.getUsername());

        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName(user1.getRole().getName())).thenReturn(Optional.of(user1.getRole()));

        // When
        boolean result = adminService.addUser(userDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNonExistingRole(){
        // Given
        Role role = new Role("role", "role");
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );

        ChangeRoleDto changeRoleDto = new ChangeRoleDto();
        changeRoleDto.setRoleName("role");
        changeRoleDto.setUsername("user1");

        when(roleRepository.findByName("role")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

        // When
        boolean result = adminService.updateRole(changeRoleDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForNonExistingUser(){
        // Given
        Role role = new Role("role", "role");
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );

        ChangeRoleDto changeRoleDto = new ChangeRoleDto();
        changeRoleDto.setRoleName("role");
        changeRoleDto.setUsername("user1");

        when(roleRepository.findByName("role")).thenReturn(Optional.of(role));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        // When
        boolean result = adminService.updateRole(changeRoleDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldSaveRoleChangesSuccessfully(){
        // Given
        Role role = new Role("role", "role");
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );

        ChangeRoleDto changeRoleDto = new ChangeRoleDto();
        changeRoleDto.setRoleName("role");
        changeRoleDto.setUsername("user1");

        when(roleRepository.findByName("role")).thenReturn(Optional.of(role));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

        // When
        boolean result = adminService.updateRole(changeRoleDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForDeletingNonExistingEvent(){
        // Given
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        Location location = new Location();
        location.setCity("city");
        Event event = new Event(
                "event",
                "event",
                user1,
                LocalDate.of(2000,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                10,
                location
        );

        when(eventRepository.findEventsByName("event")).thenReturn(Optional.empty());

        // When
        boolean result = adminService.deleteEvent("event");

        // Then
        assertFalse(result);
    }

    @Test
    void shouldDeleteEventSuccessfully(){
        // Given
        User user1 = new User(
                "user1",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        Location location = new Location();
        location.setCity("city");
        Event event = new Event(
                "event",
                "event",
                user1,
                LocalDate.of(2000,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                10,
                location
        );

        when(eventRepository.findEventsByName("event")).thenReturn(Optional.of(event));

        // When
        boolean result = adminService.deleteEvent("event");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForUpdatingNonExistingCategory(){
        // Given
        Category category = new Category("category");
        Trick trick = new Trick(
                category,
                "trick",
                "link",
                "leg",
                "desc"
        );

        TrickDto trickDto = new TrickDto(trick);

        when(categoryRepository.findByName("category")).thenReturn(Optional.empty());

        // When
        boolean result = adminService.updateTrickCategory(trickDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForUpdatingNonExistingTrick(){
        // Given
        Category category = new Category("category");
        Trick trick = new Trick(
                category,
                "trick",
                "link",
                "leg",
                "desc"
        );

        TrickDto trickDto = new TrickDto(trick);

        when(categoryRepository.findByName("category")).thenReturn(Optional.of(category));
        when(trickRepository.findByName("trick")).thenReturn(Optional.empty());

        // When
        boolean result = adminService.updateTrickCategory(trickDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldUpdateTrickSuccessfully(){
        // Given
        Category category = new Category("category");
        Trick trick = new Trick(
                category,
                "trick",
                "link",
                "leg",
                "desc"
        );

        TrickDto trickDto = new TrickDto(trick);

        when(categoryRepository.findByName("category")).thenReturn(Optional.of(category));
        when(trickRepository.findByName("trick")).thenReturn(Optional.of(trick));

        // When
        boolean result = adminService.updateTrickCategory(trickDto);

        // Then
        assertTrue(result);
    }
}
