package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Data
@Table(name="Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(
            name = "event_seq",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String localization;
    @Column(nullable = false)
    private String level;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String age;
    @Column(nullable = false)
    private int numOfParticipants;
}
