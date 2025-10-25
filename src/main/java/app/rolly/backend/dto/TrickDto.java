package app.rolly.backend.dto;

import app.rolly.backend.model.Trick;
import app.rolly.backend.model.User;
import app.rolly.backend.model.UserProgress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrickDto {
    private String categoryName;
    private String trickName;
    private String link;
    private String leg;
    private String description;
    private boolean isMastered;

    public TrickDto(Trick trick, UserProgress userProgress){
        this.categoryName = trick.getCategory().getName();
        this.trickName = trick.getName();
        this.link = trick.getLink();
        this.leg = trick.getLeg();
        this.description = trick.getDescription();
        this.isMastered = trick.getUserProgresses().contains(userProgress);
    }
}
