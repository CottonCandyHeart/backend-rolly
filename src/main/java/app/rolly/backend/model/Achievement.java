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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Achievement(
            String name, String type, String description, String picturePath,
            double requiredDistance, int requiredSessionsCount, int requiredTricksLearnedCount,
            int requiredCaloriesBurned
    ){
        this.name = name;
        this.type = type;
        this.description = description;
        this.picturePath = picturePath;

        this.requiredDistance = requiredDistance;
        this.requiredSessionsCount = requiredSessionsCount;
        this.requiredTricksLearnedCount = requiredTricksLearnedCount;
        this.requiredCaloriesBurned = requiredCaloriesBurned;
    }

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String picturePath;

    @Column(nullable = false)
    private double requiredDistance;
    @Column(nullable = false)
    private int requiredSessionsCount;
    @Column(nullable = false)
    private int requiredTricksLearnedCount;
    @Column(nullable = false)
    private int requiredCaloriesBurned;

    @ManyToMany(mappedBy = "achievements")
    private Set<User> users = new HashSet<>();


}
