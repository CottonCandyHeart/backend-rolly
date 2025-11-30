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
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Route(String name, double distance, Duration estimatedTime, User createdBy, int caloriesBurned){
        this.name = name;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.createdBy = createdBy;
        this.date = LocalDateTime.now();
        this.caloriesBurned = caloriesBurned;
    }

    public Route(String name, double distance, Duration estimatedTime, User createdBy, LocalDateTime date, int caloriesBurned){
        this.name = name;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.createdBy = createdBy;
        this.date = date;
        this.caloriesBurned = caloriesBurned;
    }

    public Route(RouteDto routeDto, User createdBy){
        Duration duration = Duration.ofSeconds(routeDto.getEstimatedTime());

        System.out.println(routeDto.getName());
        System.out.println(routeDto.getDistance());
        System.out.println(routeDto.getEstimatedTime());
        System.out.println(routeDto.getDate());
        System.out.println(createdBy.getUsername());

        this.name = routeDto.getName();
        this.distance = routeDto.getDistance();
        this.estimatedTime = duration;
        this.createdBy = createdBy;
        this.date = routeDto.getDate() != null ? routeDto.getDate() : LocalDateTime.now();

        if (routeDto.getPoints() == null || routeDto.getPoints().isEmpty())
            this.points = new ArrayList<>();
        else
            this.points = routeDto
                    .getPoints()
                    .stream()
                    .map(RoutePoint::new)
                    .peek(p -> p.setRoute(this))
                    .toList();

        if (routeDto.getPhotos() == null || routeDto.getPhotos().isEmpty())
            this.photos = new ArrayList<>();
        else
            this.photos = routeDto
                    .getPhotos()
                    .stream()
                    .map(RoutePhoto::new)
                    .peek(p -> p.setRoute(this))
                    .toList();
        this.caloriesBurned = routeDto.getCaloriesBurned();
    }

    @Column(nullable = false)
    private String name;             // eg. "Evening Ride in Kraków"
    @Column(nullable = false)
    private double distance;         // kilometers
    @Column(nullable = false)
    private Duration estimatedTime;
    @Column
    private LocalDateTime date;

    private int caloriesBurned = 0;

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
