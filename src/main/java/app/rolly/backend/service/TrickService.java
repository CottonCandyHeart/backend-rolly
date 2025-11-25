package app.rolly.backend.service;

import app.rolly.backend.dto.CategoryDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.exception.CategoryAlreadyExistsException;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.model.Category;
import app.rolly.backend.model.Trick;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.CategoryRepository;
import app.rolly.backend.repository.TrickRepository;
import app.rolly.backend.repository.UserProgressRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrickService {
    private final TrickRepository trickRepository;
    private final UserProgressRepository userProgressRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TrickDto getTrick(String trickName, String username){
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Trick> trick = trickRepository.findByName(trickName);

        if (trick.isEmpty()) throw new NotFoundException("Trick");

        return new TrickDto(trick.get(), user.get().getUserProgress());
    }

    public List<TrickDto> getTricksByCategory(String categoryName, String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserProgress userProgress = user.get().getUserProgress();
        Optional<Category> category = categoryRepository.findByName(categoryName);

        if (category.isEmpty()) throw new NotFoundException("Category");

        List<Trick> tricks = trickRepository.findTricksByCategory_Name(category.get().getName());
        if (tricks == null) tricks = new ArrayList<>();

        return tricks.stream()
                .map(trick -> new TrickDto(trick, userProgress))
                .toList();
    }

    public void setTrickAsMastered(String trickName, String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserProgress userProgress = user.get().getUserProgress();
        Optional<Trick> trick = trickRepository.findByName(trickName);

        if (userProgress == null) {
            userProgress = new UserProgress();
            userProgress.setUser(user.get());
            user.get().setUserProgress(userProgress);
            userProgress.setMasteredTricks(new HashSet<>());
            userProgressRepository.save(userProgress);
            userRepository.save(user.get());
        }

        if (trick.isEmpty()) throw new NotFoundException("Trick");

        if (!userProgress.getMasteredTricks().contains(trick.get())){
            userProgress.getMasteredTricks().add(trick.get());
            trick.get().getUserProgresses().add(userProgress);

            userProgressRepository.save(userProgress);
            trickRepository.save(trick.get());
        }
    }

    public void setTrickAsNotMastered(String trickName, String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserProgress userProgress = user.get().getUserProgress();
        Optional<Trick> trick = trickRepository.findByName(trickName);

        if (userProgress == null) {
            userProgress = new UserProgress();
            userProgress.setUser(user.get());
            user.get().setUserProgress(userProgress);
            userProgress.setMasteredTricks(new HashSet<>());
            userProgressRepository.save(userProgress);
            userRepository.save(user.get());
        }

        if (trick.isEmpty()) throw new NotFoundException("Trick");

        if (userProgress.getMasteredTricks().contains(trick.get())){
            userProgress.getMasteredTricks().remove(trick.get());
            trick.get().getUserProgresses().remove(userProgress);

            userProgressRepository.save(userProgress);
            trickRepository.save(trick.get());
        }
    }

    public List<CategoryDto> getCategories(){
        return categoryRepository.findAll().stream()
                .map(CategoryDto::new)
                .toList();
    }

    public boolean addCategory(String name){
        if (categoryRepository.existsByName(name)){
            throw new CategoryAlreadyExistsException(name);
        }
        Category category = new Category(name);
        categoryRepository.save(category);
        return true;
    }
}
