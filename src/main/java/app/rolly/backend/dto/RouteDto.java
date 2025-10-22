package app.rolly.backend.dto;

import app.rolly.backend.model.Route;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RouteDto {
    private String name;
    private double distance;
    private Duration estimatedTime;
    private String createdBy;

    public RouteDto(Route route) {
        this.name = route.getName();
        this.distance = route.getDistance();
        this.estimatedTime = route.getEstimatedTime();
        this.createdBy = route.getCreatedBy().getUsername();
    }
}
