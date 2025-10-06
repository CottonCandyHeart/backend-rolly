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
@Table(name="achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "achievement_seq")
    @SequenceGenerator(
            name = "achievement_seq",
            sequenceName = "achievement_sequence",
            allocationSize = 1
    )
    private Long id;

    public Achievement(String name, String description, String picturePath){
        this.name = name;
        this.description = description;
        this.picturePath = picturePath;
    }

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String picturePath;

    @ManyToMany(mappedBy = "achievements")
    private Set<User> users = new HashSet<>();


}
