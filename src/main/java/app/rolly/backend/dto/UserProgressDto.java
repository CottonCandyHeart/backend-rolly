package app.rolly.backend.dto;

import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProgressDto {
    private double totalDistance;
    private int totalSessions;
    private int totalTricksLearned;
    private int caloriesBurned;
    private LocalDateTime lastUpdated;
    private String username;
    private Long id;

    public UserProgressDto(UserProgress userProgress){
        this.id = userProgress.getId();
        this.totalDistance = userProgress.getTotalDistance();
        this.totalSessions = userProgress.getTotalSessions();
        this.totalTricksLearned = userProgress.getTotalTricksLearned();
        this.caloriesBurned = userProgress.getCaloriesBurned();
        this.lastUpdated = userProgress.getLastUpdated();
        this.username = userProgress.getUser().getUsername();
    }
}
