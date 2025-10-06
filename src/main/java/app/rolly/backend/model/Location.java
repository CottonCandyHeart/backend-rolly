package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(
            name = "location_seq",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    private Long id;

    public Location(String name, String city, String country, double latitude, double longitude){
        this.name = name;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Column(nullable = false)
    private String name;        // np. "Park Jordana"
    private String description;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private double latitude;    // GPS coordinates
    @Column(nullable = false)
    private double longitude;

    //@ManyToOne
    //@JoinColumn(name = "route_id")
    //private Route route;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();


}
