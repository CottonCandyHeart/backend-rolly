package app.rolly.backend.controller;

import app.rolly.backend.auth.JwtUtils;
import app.rolly.backend.dto.LoginRequest;
import app.rolly.backend.dto.UserDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value="/reg")
    public ResponseEntity<?> register(@RequestBody UserDto userDto){
        Role role = roleRepository.findByName(userDto.getRole());

        try {
            authService.registerUser(
                    userDto.getUsername(),
                    userDto.getEmail(),
                    userDto.getPasswd(),
                    LocalDate.of(userDto.getYear(), userDto.getMonth(), userDto.getDay()),
                    role
            );
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("Cannot create user: " + e, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPasswd())
            );
            String jwt = jwtUtils.generateJwtToken(authentication);
            return new ResponseEntity<>(Map.of("token", jwt), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

    }
}
