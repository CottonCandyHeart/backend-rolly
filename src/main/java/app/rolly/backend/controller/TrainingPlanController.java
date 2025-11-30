package app.rolly.backend.controller;

import app.rolly.backend.dto.TrainingPlanDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/training")
public class TrainingPlanController {
    private final TrainingPlanService trainingPlanService;

    @GetMapping("/")
    public ResponseEntity<?> getTrainings(Authentication authentication){
        return new ResponseEntity<>(
                trainingPlanService.getTrainingPlans(authentication.getName()),
                HttpStatus.OK
        );
    }

    @GetMapping("/d/{y}-{m}-{d}")
    public ResponseEntity<?> getTrainingsByDate(@PathVariable int y, @PathVariable int m, @PathVariable int d, Authentication authentication){
        return new ResponseEntity<>(
                trainingPlanService.getTrainingPlansByDate(authentication.getName(), y, m, d),
                HttpStatus.OK
        );
    }

    @GetMapping("/{y}-{m}")
    public ResponseEntity<?> getTrainingsByYearAndMonth(@PathVariable int y, @PathVariable int m, Authentication authentication){
        return new ResponseEntity<>(
                trainingPlanService.getTrainingPlansByYearAndMonth(authentication.getName(), y, m),
                HttpStatus.OK
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTraining(@RequestBody TrainingPlanDto trainingPlanDto, Authentication authentication){
        if (trainingPlanService.addTrainingPlan(trainingPlanDto, authentication.getName())){
            return new ResponseEntity<>("Training Plan created", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed creating Training Plan", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyTraining(@RequestBody TrainingPlanDto trainingPlanDto, Authentication authentication){
        if (trainingPlanService.modifyTrainingPlan(trainingPlanDto, authentication.getName())){
            return new ResponseEntity<>("Training Plan modified", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed modifying Training Plan", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<?> removeTraining(@PathVariable Long id){
        if (trainingPlanService.removeTrainingPlan(id)){
            return new ResponseEntity<>("Training Plan removed", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed: Training Plan does not exists", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/mark/{id}/{bool}")
    public ResponseEntity<?> markCompleted(@PathVariable Long id, @PathVariable boolean bool){

        if (trainingPlanService.markAsCompleted(id, bool)) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }
}
