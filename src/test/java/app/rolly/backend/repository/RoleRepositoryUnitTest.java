package app.rolly.backend.repository;

import app.rolly.backend.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryUnitTest {
    @Autowired
    private RoleRepository roleRepository;

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
}
