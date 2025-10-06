package app.rolly.backend.repository;

import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryUnitTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Role role;
    private Role savedRole;

    @BeforeEach
    void set(){
        role = new Role("testRole", "lorem ipsum");
        savedRole = roleRepository.save(role);
    }

    @Test
    void shouldSaveRole(){
        // Given

        // When

        // Then
        assertNotNull(savedRole.getId());
    }

    @Test
    void shouldFindRole(){
        // Given

        // When
        Optional<Role> foundRole = roleRepository.findById(savedRole.getId());

        // Then
        assertTrue(foundRole.isPresent());
        assertEquals("testRole", foundRole.get().getName());
        assertEquals("lorem ipsum", foundRole.get().getDescription());
    }

    @Test
    void shouldReturnNullForNonExistingRole(){
        // Given

        // When
        Optional<Role> foundRole = roleRepository.findById(-1L);

        // Then
        assertTrue(foundRole.isEmpty());
    }

    @Test
    void shouldFindAttachedUsers(){
        // Given
        User user1 = new User(
                "test1",
                "test1@test1",
                "test1Password",
                LocalDate.of(2000, 1, 1),
                role
        );
        User user2 = new User(
                "test2",
                "test2@test2",
                "test2Password",
                LocalDate.of(2001, 2, 2),
                role
        );

        role.getUsers().add(user1);
        role.getUsers().add(user2);

        userRepository.save(user1);
        userRepository.save(user2);
        roleRepository.save(role);

        // When
        Optional<Role> foundRole = roleRepository.findById(role.getId());
        List<User> users = foundRole.get().getUsers();

        // Then
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void shouldReturnEmptyUserSet(){
        // Given

        // When
        Optional<Role> foundRole = roleRepository.findById(role.getId());
        List<User> users = foundRole.get().getUsers();

        // Then
        assertTrue(users.isEmpty());
    }
}
