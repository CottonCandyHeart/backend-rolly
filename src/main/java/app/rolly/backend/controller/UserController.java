package app.rolly.backend.controller;

import app.rolly.backend.dto.*;
import app.rolly.backend.exception.UserAlreadyExistsException;
import app.rolly.backend.model.User;
import app.rolly.backend.service.AchievementService;
import app.rolly.backend.service.UserProgressService;
import app.rolly.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserProgressService userProgressService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        UserResponseDto userResponseDto = userService.getUserProfile(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserDto updateUserDto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        userService.updateUserProfile(updateUserDto, user);
        return new ResponseEntity<>("User updated", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        userService.changePassword(request, user);

        return new ResponseEntity<>("Password updated", HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestBody Long id, Authentication authentication){
        User admin = (User)authentication.getPrincipal();

        if (!admin.getRole().getName().equals("admin")) {
            return new ResponseEntity<>("Illegal role", HttpStatus.UNAUTHORIZED);
        }

        if (userService.removeUser(id)){
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User removed", HttpStatus.OK);
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getUserProgress(Authentication authentication){
        return new ResponseEntity<>(
                userProgressService.getUserProgress((User) authentication.getPrincipal()),
                HttpStatus.OK
        );
    }

    @PostMapping("/progress")
    public ResponseEntity<?> updateUserProgress(@RequestBody UserProgressDto userProgressDto, Authentication authentication){
        userProgressService.updateStats(userProgressDto, (User) authentication.getPrincipal());
        return new ResponseEntity<>("User Progress updated", HttpStatus.OK);
    }

    @PostMapping("/meas")
    public ResponseEntity<?> updateMeasurements(@RequestBody UserMeasurementsDto userMeasurementsDto, Authentication authentication){
        userService.updateMeasurements(userMeasurementsDto, (User) authentication.getPrincipal());
        return new ResponseEntity<>("Measurements updated", HttpStatus.OK);
    }

    @GetMapping("/meas")
    public ResponseEntity<?> getMeasurements(Authentication authentication){
        return new ResponseEntity<>(userService.getMeasurements((User) authentication.getPrincipal()), HttpStatus.OK);
    }
}
