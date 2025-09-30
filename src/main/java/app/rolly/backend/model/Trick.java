package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name="trick")
public class Trick {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trick_seq")
    @SequenceGenerator(
            name = "trick_seq",
            sequenceName = "trick_sequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "cathegory_id", nullable = false)
    private Cathegory cathegory;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String videoLink;
    @Column(nullable = false)
    private String description;
}
