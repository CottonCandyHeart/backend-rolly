package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="trick")
public class Trick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Trick(Category category, String name, String link, String leg, String description){
        this.category = category;
        this.name = name;
        this.link = link;
        this.leg = leg;
        this.description = description;
    }

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String link;
    @Column(nullable = false)
    private String leg;
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "user_mastered_tricks",
            joinColumns = @JoinColumn(name = "trick_id"),
            inverseJoinColumns = @JoinColumn(name = "user_progress_id")
    )
    Set<UserProgress> userProgresses = new HashSet<>();
}
