package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
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

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String videoLink;
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
