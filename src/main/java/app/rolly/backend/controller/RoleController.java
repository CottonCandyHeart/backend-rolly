package app.rolly.backend.controller;

import app.rolly.backend.dto.RoleDto;
import app.rolly.backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/")
    public ResponseEntity<?> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestBody RoleDto roleDto){
        System.out.println("ROLE DTO: " + roleDto);
        if (roleService.addRole(roleDto)) return new ResponseEntity<>("Role added", HttpStatus.OK);

        return new ResponseEntity<>("Failed adding role", HttpStatus.BAD_REQUEST);
    }
}
