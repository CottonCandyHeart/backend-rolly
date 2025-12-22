package app.rolly.backend.service;

import app.rolly.backend.dto.RoleDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    final RoleRepository roleRepository;

    public List<RoleDto> getRoles(){
        return roleRepository.findAll().stream()
                .map(RoleDto::new)
                .toList();
    }


    public boolean addRole(RoleDto roleDto){
        Optional<Role> roleOpt = roleRepository.findByName(roleDto.getName());
        if (roleOpt.isPresent()) return false;

        Role role = new Role(roleDto.getName(), roleDto.getDescription());
        roleRepository.save(role);

        return true;
    }
}
