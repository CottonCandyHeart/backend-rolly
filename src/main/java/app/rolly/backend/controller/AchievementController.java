package app.rolly.backend.controller;

import app.rolly.backend.dto.AchievementDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value="/achievement")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping("/")
    public ResponseEntity<?> getAchievements(){
        Set<AchievementDto> achievementDtos = achievementService.getAllAchievements();
        return new ResponseEntity<>(achievementDtos, HttpStatus.OK);
    }

    @GetMapping("/user-achievements")
    public ResponseEntity<?> getUserAchievements(Authentication authentication){
        Set<AchievementDto> achievementDtos = achievementService.getUserAchievements(
                authentication.getName());

        return new ResponseEntity<>(achievementDtos, HttpStatus.OK);
    }

    @PostMapping("/add/{achId}")
    public ResponseEntity<?> addAchievementToUser(@PathVariable Long achId, Authentication authentication){
        boolean result = achievementService.addAchievementToUser(
                authentication.getName(),
                achId
        );

        if (!result) return new ResponseEntity<>("Failed: Achievement already added", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Achievement added", HttpStatus.OK);
    }

    @PostMapping("/remove/{achId}")
    public ResponseEntity<?> removeAchievementFromUser(@PathVariable Long achId, Authentication authentication){
        boolean result;
        try {
            result = achievementService.removeAchievementFromUser(
                    authentication.getName(),
                    achId
            );
        } catch (RuntimeException e){
            return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!result) return new ResponseEntity<>("Failed: Achievement does not exist", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Achievement removed", HttpStatus.OK);
    }


}
