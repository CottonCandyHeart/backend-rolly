package app.rolly.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name="Trick")
public class Trick {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(
            name = "event_seq",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    private long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String videoLink;
    @Column(nullable = false)
    private String description;
}
