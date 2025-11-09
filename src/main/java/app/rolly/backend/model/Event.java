package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(
            name = "event_seq",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    private Long id;

    public Event(User organizer, LocalDate date, LocalTime time, String level, String type, String age, int numOfParticipants, Location location){
        this.organizer = organizer;
        this.date = date;
        this.time = time;
        this.level = level;
        this.type = type;
        this.age = age;
        this.numOfParticipants = numOfParticipants;
        this.location = location;
        this.city = this.location.getCity();

    }

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(nullable = false)
    private String level;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String age;
    @Column(nullable = false)
    private int numOfParticipants;
    @Column(nullable = false)
    private String city;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToMany(mappedBy = "attendedEvents")
    private Set<User> attendee = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

}
