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
        UserResponseDto userResponseDto = userService.getUserProfile(authentication.getName());
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserDto updateUserDto, Authentication authentication){
        userService.updateUserProfile(updateUserDto, authentication.getName());
        return new ResponseEntity<>("User updated", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication){
        userService.changePassword(request, authentication.getName());

        return new ResponseEntity<>("Password updated", HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestBody UserResponseDto userResponseDto, Authentication authentication){
        String admin = authentication.getName();
        System.out.println(userResponseDto.getUsername());

        if (!admin.equals("admin")) {
            System.out.println("admin if");
            return new ResponseEntity<>("Illegal role", HttpStatus.UNAUTHORIZED);
        }

        if (!userService.removeUser(userResponseDto.getUsername())){
            System.out.println("username if");
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }

        System.out.println("its fine");
        return new ResponseEntity<>("User removed", HttpStatus.OK);
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getUserProgress(Authentication authentication){
        return new ResponseEntity<>(
                userProgressService.getUserProgress(authentication.getName()),
                HttpStatus.OK
        );
    }

    @PostMapping("/progress")
    public ResponseEntity<?> updateUserProgress(@RequestBody UserProgressDto userProgressDto, Authentication authentication){
        userProgressService.updateStats(userProgressDto, authentication.getName());
        return new ResponseEntity<>("User Progress updated", HttpStatus.OK);
    }

    @PostMapping("/meas")
    public ResponseEntity<?> updateMeasurements(@RequestBody UserMeasurementsDto userMeasurementsDto, Authentication authentication){
        userService.updateMeasurements(userMeasurementsDto, authentication.getName());
        return new ResponseEntity<>("Measurements updated", HttpStatus.OK);
    }

    @GetMapping("/meas")
    public ResponseEntity<?> getMeasurements(Authentication authentication){
        return new ResponseEntity<>(userService.getMeasurements(authentication.getName()), HttpStatus.OK);
    }
}
