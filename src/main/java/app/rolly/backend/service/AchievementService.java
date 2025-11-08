package app.rolly.backend.service;

import app.rolly.backend.dto.AchievementDto;
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

    // TODO przydzielanie osiągnięć użytkownikom (np. „Pierwszy trening!”, „100 km razem!”).
    //  if (user.getUserProgress().getTotalDistance() > 100)
    //    awardAchievement(user, "100 km legend");

    public Set<AchievementDto> getUserAchievements(User user) {
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
    public boolean addAchievementToUser(User user, Long achievementId){
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        if (user.getAchievements().contains(achievement)) {
            return false;
        }

        user.getAchievements().add(achievement);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean removeAchievementFromUser(User user, Long achievementId){
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        if (!user.getAchievements().contains(achievement)) {
            return false;
        }

        user.getAchievements().remove(achievement);
        userRepository.save(user);
        return true;
    }
}
