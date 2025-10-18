package app.rolly.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrickDto {
    private String categoryName;
    private String trickName;
    private String link;
    private String description;
}
