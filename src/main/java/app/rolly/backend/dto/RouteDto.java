package app.rolly.backend.dto;

import app.rolly.backend.model.Route;
import app.rolly.backend.model.RoutePhoto;
import app.rolly.backend.model.RoutePoint;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RouteDto {
    private String name;
    private double distance;
    private Duration estimatedTime;
    private String createdBy;
    private LocalDate date;
    private List<RoutePointDto> points;
    private List<RoutePhotoDto> photos;

    public RouteDto(Route route) {
        this.name = route.getName();
        this.distance = route.getDistance();
        this.estimatedTime = route.getEstimatedTime();
        this.createdBy = route.getCreatedBy().getUsername();
        this.date = route.getDate();
        this.points = route.getPoints().stream().map(RoutePointDto::new).toList();
        this.photos = route.getPhotos().stream().map(RoutePhotoDto::new).toList();

    }
}
