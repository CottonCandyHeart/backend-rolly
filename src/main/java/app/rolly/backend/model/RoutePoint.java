package app.rolly.backend.model;

import app.rolly.backend.dto.RoutePointDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_point")
@NoArgsConstructor
@Getter
@Setter
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public RoutePoint(RoutePointDto routePointDto){
        this.latitude = routePointDto.getLatitude();
        this.longitude = routePointDto.getLongitude();
        this.timestamp = routePointDto.getTimestamp();
    }

    @Column(nullable = false)
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
