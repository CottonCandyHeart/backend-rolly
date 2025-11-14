package app.rolly.backend.service;

import app.rolly.backend.dto.UserProgressDto;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProgressService {
    private final UserProgressRepository userProgressRepository;

    public UserProgressDto getUserProgress(User user){
        return new UserProgressDto(user.getUserProgress());
    }

    public void updateStats(UserProgressDto userProgressDto, User user){
        UserProgress userProgress = user.getUserProgress();

        userProgress.setTotalDistance(userProgressDto.getTotalDistance());
        userProgress.setTotalSessions(userProgressDto.getTotalSessions());
        userProgress.setTotalTricksLearned(userProgressDto.getTotalTricksLearned());
        userProgress.setCaloriesBurned(userProgressDto.getCaloriesBurned());
        userProgress.setLastUpdated(userProgressDto.getLastUpdated());

        if (userProgress.getTotalTricksLearned() >= 80) user.setUserLevel("Advanced");
        else if (userProgress.getTotalTricksLearned() >= 30) user.setUserLevel("Mid");

        userProgressRepository.save(userProgress);
        user.setUserProgress(userProgress);
    }

}
