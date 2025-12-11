package app.rolly.backend.dto;

import app.rolly.backend.model.City;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CityDto {
    private Long id;
    private String city;

    public CityDto(City c) {
        this.id = c.getId();
        this.city = c.getCity();
    }
}
