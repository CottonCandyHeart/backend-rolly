package app.rolly.backend.service;

import app.rolly.backend.dto.UserDto;
import app.rolly.backend.exception.UserAlreadyExistsException;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.RoleRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public boolean registerUser(UserDto userDto) {
        System.out.println("Checking username: " + userDto.getUsername());
        System.out.println("Found user: " + userRepository.findByUsername(userDto.getUsername()));
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new UserAlreadyExistsException(userDto.getUsername());
        }

        Optional<Role> role;
        if ((role = roleRepository.findByName(userDto.getRole())).isEmpty()){
            role = Optional.of(new Role(userDto.getRole(), ""));
            roleRepository.save(role.get());
        }

        userRepository.save(
                new User(
                    userDto.getUsername(),
                    userDto.getEmail(),
                    passwordEncoder.encode(userDto.getPasswd()),
                    userDto.getBirthday(),
                    role.get()
                )
        );

        return true;
    }

}
