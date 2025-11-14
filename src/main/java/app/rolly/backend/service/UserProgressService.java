package app.rolly.backend.service;

import app.rolly.backend.dto.UserProgressDto;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.UserProgressRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProgressService {
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;

    public UserProgressDto getUserProgress(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return new UserProgressDto(user.get().getUserProgress());
    }

    public void updateStats(UserProgressDto userProgressDto, String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserProgress userProgress = user.get().getUserProgress();

        userProgress.setTotalDistance(userProgressDto.getTotalDistance());
        userProgress.setTotalSessions(userProgressDto.getTotalSessions());
        userProgress.setTotalTricksLearned(userProgressDto.getTotalTricksLearned());
        userProgress.setCaloriesBurned(userProgressDto.getCaloriesBurned());
        userProgress.setLastUpdated(userProgressDto.getLastUpdated());

        if (userProgress.getTotalTricksLearned() >= 80) user.get().setUserLevel("Advanced");
        else if (userProgress.getTotalTricksLearned() >= 30) user.get().setUserLevel("Mid");

        userProgressRepository.save(userProgress);
        user.get().setUserProgress(userProgress);
    }

}
