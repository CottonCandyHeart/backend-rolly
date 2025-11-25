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
    private final TrickService trickService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getTrick(@PathVariable String name, Authentication authentication){
        return new ResponseEntity<>(trickService.getTrick(name, authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/{name}")
    public ResponseEntity<?> setTrickAsMastered(@PathVariable String name, Authentication authentication){
        trickService.setTrickAsMastered(name, authentication.getName());
        return new ResponseEntity<>("Trick set as mastered", HttpStatus.OK);
    }
    @PostMapping("/remove/{name}")
    public ResponseEntity<?> setTrickAsNotMastered(@PathVariable String name, Authentication authentication){
        trickService.setTrickAsNotMastered(name, authentication.getName());
        return new ResponseEntity<>("Trick set as not mastered", HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetProgress(Authentication authentication) {
        trickService.resetProgress(authentication.getName());
        return new ResponseEntity<>("Reset Progress", HttpStatus.OK);
    }

    @GetMapping("/by-cat/{category}")
    public ResponseEntity<?> getTrickByCategoryName(@PathVariable String category, Authentication authentication){
        return new ResponseEntity<>(trickService.getTricksByCategory(category, authentication.getName()), HttpStatus.OK);
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
