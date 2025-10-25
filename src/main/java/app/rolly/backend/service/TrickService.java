package app.rolly.backend.service;

import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.model.Category;
import app.rolly.backend.model.Trick;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.CategoryRepository;
import app.rolly.backend.repository.TrickRepository;
import app.rolly.backend.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrickService {
    private final TrickRepository trickRepository;
    private final UserProgressRepository userProgressRepository;
    private final CategoryRepository categoryRepository;

    public TrickDto getTrick(String trickName){
        Trick trick = trickRepository.findByName(trickName);

        TrickDto trickDto = new TrickDto();
        trickDto.setTrickName(trick.getName());
        trickDto.setCategoryName(trick.getCategory().getName());
        trickDto.setDescription(trick.getDescription());
        trickDto.setLink(trick.getLink());
        trickDto.setLeg(trick.getLeg());

        return trickDto;
    }

    public List<TrickDto> getTricksByCategory(String categoryName, UserProgress userProgress){
        Category category = categoryRepository.findByName(categoryName);

        return trickRepository.findTricksByCategory_Name(category.getName()).stream()
                .map(trick -> new TrickDto(trick, userProgress))
                .toList();
    }

    public void setTrickAsMastered(String trickName, UserProgress userProgress){
        Trick trick = trickRepository.findByName(trickName);

        if (!userProgress.getMasteredTricks().contains(trick)){
            userProgress.getMasteredTricks().add(trick);
            userProgressRepository.save(userProgress);
        }
    }
}
