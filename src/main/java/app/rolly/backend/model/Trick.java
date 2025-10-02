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
    private Long id;

    public Trick(Category category, String name, String videoLink, String description){
        this.category = category;
        this.name = name;
        this.videoLink = videoLink;
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String videoLink;
    @Column(nullable = false)
    private String description;
}
