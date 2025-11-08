package app.rolly.backend.dto;

import app.rolly.backend.model.Achievement;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementDto {
    private Long id;
    private String name;
    private String type;
    private String description;
    private String picturePath;
    private double requiredDistance;
    private int requiredSessionsCount;
    private int requiredTricksLearnedCount;
    private int requiredCaloriesBurned;

    public AchievementDto(Achievement achievement) {
        this.id = achievement.getId();
        this.name = achievement.getName();
        this.type = achievement.getType();
        this.description = achievement.getDescription();
        this.picturePath = achievement.getPicturePath();

        this.requiredDistance = achievement.getRequiredDistance();
        this.requiredSessionsCount = achievement.getRequiredSessionsCount();
        this.requiredTricksLearnedCount = achievement.getRequiredTricksLearnedCount();
        this.requiredCaloriesBurned = achievement.getRequiredCaloriesBurned();
    }
}
