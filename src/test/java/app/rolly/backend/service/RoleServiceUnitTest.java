package app.rolly.backend.service;

import app.rolly.backend.dto.RoleDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceUnitTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void shouldReturnAllRoleDtoList(){
        // Given
        Role role1 = new Role("role1", "role1");
        Role role2 = new Role("role2", "role2");

        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));

        // When
        List<RoleDto> result = roleService.getRoles();

        // Then
        assertEquals(2, result.size());
        assertEquals("role1", result.getFirst().getName());
        assertEquals("role2", result.getLast().getName());
    }

    @Test
    void shouldReturnFalseForAddingExistingRole(){
        // Given
        Role role1 = new Role("role1", "role1");
        RoleDto roleDto = new RoleDto(role1);

        when(roleRepository.findByName("role1")).thenReturn(Optional.of(role1));

        // When
        boolean result = roleService.addRole(roleDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldAddRoleSuccessfully(){
        // Given
        Role role1 = new Role("role1", "role1");
        RoleDto roleDto = new RoleDto(role1);

        when(roleRepository.findByName("role1")).thenReturn(Optional.empty());

        // When
        boolean result = roleService.addRole(roleDto);

        // Then
        assertTrue(result);
    }
}
