package app.rolly.backend.controller;

import app.rolly.backend.dto.ChangePasswordRequest;
import app.rolly.backend.dto.UpdateUserDto;
import app.rolly.backend.dto.UserResponseDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        UserResponseDto userResponseDto = userService.getUserProfile(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserDto updateUserDto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        userService.updateUserProfile(updateUserDto, user);
        return new ResponseEntity<>("User updated", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        try {
            userService.changePassword(request, user);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Changing password failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


        return new ResponseEntity<>("Password updated", HttpStatus.OK);
    }
}
