package app.rolly.backend.repository;

import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryUnitTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private LocalDate date;
    private User user;
    private User savedUser;

    @BeforeEach
    void set(){
        role = new Role("testRole", "lorem ipsum");
        roleRepository.save(role);

        date = LocalDate.of(2000, 1, 1);

        user = new User(
                "testUsername",
                "test@test",
                "testPassword",
                date,
                role
        );
        savedUser = userRepository.save(user);
    }

    @Test
    void shouldSaveUser(){
        // Given

        // When

        // Then
        assertNotNull(savedUser.getId());
    }

    @Test
    void shouldFindUser(){
        // Given

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("testUsername", foundUser.get().getUsername());
        assertEquals("test@test", foundUser.get().getEmail());
        assertEquals("testPassword", foundUser.get().getHashedPasswd());
        assertEquals(date, foundUser.get().getDateOfBirth());
        assertEquals(role, foundUser.get().getRole());
    }
}
