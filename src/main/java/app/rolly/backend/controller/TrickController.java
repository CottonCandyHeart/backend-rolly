package app.rolly.backend.controller;

import app.rolly.backend.dto.CategoryDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.TrickRepository;
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

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllTricks(){
        return new ResponseEntity<>(trickService.getAllTricks(), HttpStatus.OK);
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

    @DeleteMapping("/del-category/{name}")
    public ResponseEntity<?> deleteCategory(@PathVariable String name){
        if (trickService.deleteCategory(name)){
            return new ResponseEntity<>("Deleted category", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed deleting category", HttpStatus.OK);
    }

    @PostMapping("/add-trick")
    public ResponseEntity<?> addTrick(@RequestBody TrickDto trickDto){
        trickService.addTrick(trickDto);
        return new ResponseEntity<>("Trick added successfully", HttpStatus.OK);
    }

    @DeleteMapping("/del-trick/{name}")
    public ResponseEntity<?> deleteTrick(@PathVariable String name){
        if (trickService.deleteTrick(name)){
            return new ResponseEntity<>("Deleted trick", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed deleting trick", HttpStatus.OK);
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getProgress(Authentication authentication){
        return new ResponseEntity<>(trickService.getProgress(authentication.getName()),HttpStatus.OK);
    }
}
