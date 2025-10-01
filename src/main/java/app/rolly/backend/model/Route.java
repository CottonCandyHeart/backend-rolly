package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
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
    }

    @Column(nullable = false)
    private String name;             // np. "Evening Ride in Kraków"
    @Column(nullable = false)
    private double distance;         // kilometers
    @Column(nullable = false)
    private Duration estimatedTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<Location> points;    // waypoint list
}
