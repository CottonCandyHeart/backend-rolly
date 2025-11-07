package app.rolly.backend.model;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.repository.UserRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_seq")
    @SequenceGenerator(
            name = "route_seq",
            sequenceName = "route_sequence",
            allocationSize = 1
    )
    private Long id;

    public Route(String name, double distance, Duration estimatedTime, User createdBy){
        this.name = name;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.createdBy = createdBy;
        this.date = LocalDate.now();
    }

    public Route(RouteDto routeDto, User createdBy){
        this.name = routeDto.getName();
        this.distance = routeDto.getDistance();
        this.estimatedTime = routeDto.getEstimatedTime();
        this.createdBy = createdBy;
        this.date = routeDto.getDate();
        this.points = routeDto.getPoints().stream().map(RoutePoint::new).toList();
        this.photos = routeDto.getPhotos().stream().map(RoutePhoto::new).toList();
    }

    @Column(nullable = false)
    private String name;             // eg. "Evening Ride in Kraków"
    @Column(nullable = false)
    private double distance;         // kilometers
    @Column(nullable = false)
    private Duration estimatedTime;
    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    // no photos
    //@Column(columnDefinition = "TEXT")
    //private String encodedPath;

    // photos
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutePoint> points = new ArrayList<>();

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutePhoto> photos = new ArrayList<>();
}
