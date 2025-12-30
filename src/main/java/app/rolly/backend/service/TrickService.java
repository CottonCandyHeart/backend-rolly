package app.rolly.backend.service;

import app.rolly.backend.dto.CategoryDto;
import app.rolly.backend.dto.CategoryProgressDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.exception.CategoryAlreadyExistsException;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.exception.TrickAlreadyExistsException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public List<TrickDto> getAllTricks(){
        return trickRepository.findAll().stream()
                .map(TrickDto::new)
                .toList();
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
            userProgress.setTotalTricksLearned(userProgress.getTotalTricksLearned()+1);
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
            userProgress.setTotalTricksLearned(userProgress.getTotalTricksLearned()-1);
            trick.get().getUserProgresses().remove(userProgress);

            userProgressRepository.save(userProgress);
            trickRepository.save(trick.get());
        }
    }

    public void resetProgress(String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserProgress userProgress = user.get().getUserProgress();

        if (userProgress != null) {
            List<Trick> tricks = trickRepository.findByUserProgresses_Id(userProgress.getId());
            for (Trick t : tricks){
                t.getUserProgresses().remove(userProgress);
                trickRepository.save(t);
            }
            userProgress.getMasteredTricks().clear();
            userProgress.setTotalTricksLearned(0);
            userProgressRepository.save(userProgress);
        }
    }

    public List<CategoryDto> getCategories(){
        return categoryRepository.findAll().stream()
                .map(CategoryDto::new)
                .toList();
    }

    public boolean addCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new CategoryAlreadyExistsException(name);
        }
        Category category = new Category(name);
        categoryRepository.save(category);
        return true;
    }

    @Transactional
    public boolean deleteCategory(String name){
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isEmpty()) return false;

        List<Trick> tricks = category.get().getTricks();

        for (Trick t : tricks) {
            t.setCategory(null);
            trickRepository.save(t);
        }
        category.get().getTricks().clear();

        categoryRepository.delete(category.get());

        return true;
    }

    public void addTrick(TrickDto trickDto){
        if (trickRepository.existsByName(trickDto.getTrickName())) {
            throw new TrickAlreadyExistsException(trickDto.getTrickName());
        }
        Optional<Category> category = categoryRepository.findByName(trickDto.getCategoryName());
        if (category.isEmpty()) throw new NotFoundException(trickDto.getCategoryName());

        Trick trick = new Trick(
                category.get(),
                trickDto.getTrickName(),
                trickDto.getLink(),
                trickDto.getLeg(),
                trickDto.getDescription()
        );
        trickRepository.save(trick);
    }

    @Transactional
    public boolean deleteTrick(String name){
        Optional<Trick> trick = trickRepository.findByName(name);
        if (trick.isEmpty()) return false;

        Category category = trick.get().getCategory();
        category.getTricks().remove(trick.get());

        trickRepository.delete(trick.get());

        return true;
    }

    public List<CategoryProgressDto> getProgress(String username){
        Optional<User> userOpt = userRepository.findByUsername(username);
        List<Category> categories = categoryRepository.findAll();

        if (userOpt.isEmpty()) throw new NotFoundException("User");
        User user = userOpt.get();

        List<CategoryProgressDto> categoryProgressDtos = new ArrayList<>();

        for (Category c : categories){
            List<Trick> tricks = c.getTricks();
            int allTricks = tricks.size();
            int mastered = 0;

            UserProgress up = user.getUserProgress();

            if (up != null) {
                Set<Trick> tricks2 = up.getMasteredTricks();
                for (Trick t : tricks2){
                    if (tricks.contains(t)) mastered++;
                }
            }

            categoryProgressDtos.add(new CategoryProgressDto(
                    c.getName(),
                    allTricks,
                    mastered
            ));
        }

        return categoryProgressDtos;
    }
}
