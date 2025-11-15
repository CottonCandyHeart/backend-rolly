package app.rolly.backend.service;

import app.rolly.backend.dto.AchievementDto;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.model.Achievement;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.AchievementRepository;
import app.rolly.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public Set<AchievementDto> getUserAchievements(String username) {
        User user = userRepository.findByUsername(username).get();
        return user.getAchievements().stream()
                .map(AchievementDto::new)
                .collect(Collectors.toSet());
    }

    public Set<AchievementDto> getAllAchievements(){
        return achievementRepository.findAll().stream()
                .map(AchievementDto::new)
                .collect(Collectors.toSet());
    }

    @Transactional
    public boolean addAchievementToUser(String username, Long achievementId){
        User user = userRepository.findByUsername(username).get();
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new NotFoundException("Achievement"));

        if (user.getAchievements().contains(achievement)) {
            return false;
        }

        user.getAchievements().add(achievement);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean removeAchievementFromUser(String username, Long achievementId){
        User user = userRepository.findByUsername(username).get();
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new NotFoundException("Achievement"));

        if (!user.getAchievements().contains(achievement)) {
            return false;
        }

        user.getAchievements().remove(achievement);
        userRepository.save(user);
        return true;
    }
}
