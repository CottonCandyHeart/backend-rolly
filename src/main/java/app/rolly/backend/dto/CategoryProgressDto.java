package app.rolly.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryProgressDto {
    private String categoryName;
    private int totalTricks;
    private int masteredTricks;

    public CategoryProgressDto(String name, int total, int mastered){
        this.categoryName = name;
        this.totalTricks = total;
        this.masteredTricks = mastered;
    }
}
