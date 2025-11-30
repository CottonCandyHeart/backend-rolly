package app.rolly.backend.dto;

import app.rolly.backend.model.Route;
import app.rolly.backend.model.RoutePhoto;
import app.rolly.backend.model.RoutePoint;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RouteDto {
    private String name;
    private double distance;
    //private Duration estimatedTime;
    private long estimatedTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime date;
    private List<RoutePointDto> points;
    private List<RoutePhotoDto> photos;
    private int caloriesBurned;

    public RouteDto(Route route) {
        this.name = route.getName();
        this.distance = route.getDistance();
        this.estimatedTime = route.getEstimatedTime().toSeconds();
        this.date = route.getDate();
        this.points = route.getPoints().stream().map(RoutePointDto::new).toList();
        this.photos = route.getPhotos().stream().map(RoutePhotoDto::new).toList();
        this.caloriesBurned = route.getCaloriesBurned();

    }
}
