package app.rolly.backend.controller;

import app.rolly.backend.dto.CategoryDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.TrickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trick")
@RequiredArgsConstructor
public class TrickController {
    private TrickService trickService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getTrick(@PathVariable String name, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(trickService.getTrick(name, user.getUserProgress()), HttpStatus.OK);
    }

    @PostMapping("/{name}")
    public ResponseEntity<?> setTrickAsMastered(@PathVariable String name, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        trickService.setTrickAsMastered(name, user.getUserProgress());
        return new ResponseEntity<>("Trick set as mastered", HttpStatus.OK);
    }

    @GetMapping("/{category}")
    public ResponseEntity<?> getTrickByCategoryName(@PathVariable String category, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(trickService.getTricksByCategory(category, user.getUserProgress()), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(){
        return new ResponseEntity<>(trickService.getCategories(), HttpStatus.OK);
    }

    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto){
        trickService.addCategory(categoryDto.getName());
        return new ResponseEntity<>("Category added successfully", HttpStatus.OK);
    }
}
