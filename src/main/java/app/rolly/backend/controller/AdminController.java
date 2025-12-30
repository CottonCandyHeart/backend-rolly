package app.rolly.backend.controller;

import app.rolly.backend.dto.ChangeRoleDto;
import app.rolly.backend.dto.RoleDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.dto.UserDto;
import app.rolly.backend.service.AdminService;
import app.rolly.backend.service.UserProgressService;
import app.rolly.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/isAdmin")
    public ResponseEntity<?> isAdmin(Authentication authentication){

        if (authentication.getName().equals("admin")){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUserList(){
        return new ResponseEntity<>(adminService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto){
        if (adminService.addUser(userDto)) return new ResponseEntity<>("User added", HttpStatus.OK);

        return new ResponseEntity<>("Failed adding user", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/role")
    public ResponseEntity<?> changeRole(@RequestBody ChangeRoleDto changeRoleDto){
        if (adminService.updateRole(changeRoleDto)) return new ResponseEntity<>("Role updated", HttpStatus.OK);

        return new ResponseEntity<>("Failed updating role", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/update")
    public ResponseEntity<?> changeTrickCategory(@RequestBody TrickDto trickDto){
        if (adminService.updateTrickCategory(trickDto)) return new ResponseEntity<>("Role updated", HttpStatus.OK);

        return new ResponseEntity<>("Failed updating category", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/del/event/{name}")
    public ResponseEntity<?> deleteEvent(@PathVariable String name){
        boolean deleted = adminService.deleteEvent(name);

        if (deleted) {
            return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot delete event", HttpStatus.BAD_REQUEST);
    }
}
