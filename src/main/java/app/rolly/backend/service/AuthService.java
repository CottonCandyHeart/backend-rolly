package app.rolly.backend.service;

import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String email, String passwd, LocalDate dateOfBirth, Role role){
        userRepository.save(
                new User(username, email, passwordEncoder.encode(passwd), dateOfBirth, role)
        );
    }

    public boolean login(String username, String passwd){
        User user = userRepository.findByUsername(username);
        if (user == null) return false;

        return (passwordEncoder.matches(passwd, user.getHashedPasswd()));
    }

}
