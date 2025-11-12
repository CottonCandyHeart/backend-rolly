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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrickService {
    private final TrickRepository trickRepository;
    private final UserProgressRepository userProgressRepository;
    private final CategoryRepository categoryRepository;

    public TrickDto getTrick(String trickName, UserProgress userProgress){
        Trick trick = trickRepository.findByName(trickName);

        if (trick == null) throw new NotFoundException("Trick");

        return new TrickDto(trick, userProgress);
    }

    public List<TrickDto> getTricksByCategory(String categoryName, UserProgress userProgress){
        Category category = categoryRepository.findByName(categoryName);

        if (category == null) throw new NotFoundException("Category");

        List<Trick> tricks = trickRepository.findTricksByCategory_Name(category.getName());
        if (tricks == null) tricks = new ArrayList<>();

        return tricks.stream()
                .map(trick -> new TrickDto(trick, userProgress))
                .toList();
    }

    public void setTrickAsMastered(String trickName, UserProgress userProgress){
        Trick trick = trickRepository.findByName(trickName);

        if (trick == null) throw new NotFoundException("Trick");

        if (!userProgress.getMasteredTricks().contains(trick)){
            userProgress.getMasteredTricks().add(trick);
            userProgressRepository.save(userProgress);
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
