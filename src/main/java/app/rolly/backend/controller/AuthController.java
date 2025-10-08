package app.rolly.backend.controller;

import app.rolly.backend.dto.UserDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final RoleRepository roleRepository;
    private final AuthService authService;

    // TODO rejestracja użytkownika w bazie, hashowanie hasła
    /*
    @GetMapping(value = "/{msg}")
    public String f(@PathVariable String msg){
        return msg;
    }
     */

    @GetMapping(value="/reg")
    public void register(UserDto userDto){
        Role role = roleRepository.findByName(userDto.getRole());

        authService.registerUser(
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPasswd(),
                LocalDate.of(userDto.getYear(), userDto.getMonth(), userDto.getDay()),
                role
        );
    }
}
