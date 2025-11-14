package app.rolly.backend.dto;

import app.rolly.backend.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMeasurementsDto {
    private double weight;
    private int height;

    public UserMeasurementsDto(double weight, int height) {
        this.weight = weight;
        this.height = height;
    }
}
