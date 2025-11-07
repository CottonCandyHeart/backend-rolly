package app.rolly.backend.dto;

import app.rolly.backend.model.RoutePhoto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoutePhotoDto {
    private Long id;
    private double latitude;
    private double longitude;
    private String imageUrl;
    private LocalDateTime timestamp;

    public RoutePhotoDto(RoutePhoto routePhoto){
        this.id = routePhoto.getId();
        this.latitude = routePhoto.getLatitude();
        this.longitude = routePhoto.getLongitude();
        this.imageUrl = routePhoto.getImageUrl();
        this.timestamp = routePhoto.getTimestamp();
    }
}
