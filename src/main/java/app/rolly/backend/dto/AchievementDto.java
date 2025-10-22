package app.rolly.backend.dto;

import app.rolly.backend.model.Achievement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementDto {
    private String name;
    private String description;
    private String picturePath;

    public AchievementDto(Achievement achievement) {
        this.name = achievement.getName();
        this.description = achievement.getDescription();
        this.picturePath = achievement.getPicturePath();
    }
}
