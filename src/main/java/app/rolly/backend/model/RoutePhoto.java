package app.rolly.backend.model;

import app.rolly.backend.dto.RoutePhotoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "route_photo")
public class RoutePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public RoutePhoto(RoutePhotoDto routePhotoDto){
        this.latitude = routePhotoDto.getLatitude();
        this.longitude = routePhotoDto.getLongitude();
        this.imageUrl = routePhotoDto.getImageUrl();
        this.timestamp = routePhotoDto.getTimestamp();
    }

    private double latitude;
    private double longitude;
    private String imageUrl;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
