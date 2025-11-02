package app.rolly.backend.dto;

import app.rolly.backend.model.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDto {
    private String name;
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public LocationDto(Location location){
        this.name = location.getName();
        this.city = location.getCity();
        this.country = location.getCountry();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
