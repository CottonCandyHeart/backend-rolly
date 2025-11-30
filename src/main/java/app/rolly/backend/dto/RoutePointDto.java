package app.rolly.backend.dto;

import app.rolly.backend.model.RoutePoint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoutePointDto {
    private Long id;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

    public RoutePointDto(RoutePoint routePoint){
        this.id = routePoint.getId();
        this.latitude = routePoint.getLatitude();
        this.longitude = routePoint.getLongitude();
        this.timestamp = routePoint.getTimestamp();
    }
}
