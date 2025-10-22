package app.rolly.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "route_photo")
public class RoutePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_seq")
    @SequenceGenerator(
            name = "route_seq",
            sequenceName = "route_sequence",
            allocationSize = 1
    )
    private Long id;

    private double latitude;
    private double longitude;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
