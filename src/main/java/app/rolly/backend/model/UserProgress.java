package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "user_progress_seq")
    @SequenceGenerator(
            name = "user_progress_seq",
            sequenceName = "user_progress_sequence",
            allocationSize = 1
    )
    private Long id;

    public UserProgress(double totalDistance, int totalSessions, int totalTricksLearned, int caloriesBurned, LocalDateTime lastUpdated, User user){
        this.totalDistance = totalDistance;
        this.totalSessions = totalSessions;
        this.totalTricksLearned = totalTricksLearned;
        this.caloriesBurned = caloriesBurned;
        this.lastUpdated = lastUpdated;
        this.user = user;
    }

    @Column(nullable = false)
    private double totalDistance;    // sum kilometers
    @Column(nullable = false)
    private int totalSessions;       // number of sessions
    @Column(nullable = false)
    private int totalTricksLearned;  // number of tricks learned
    @Column(nullable = false)
    private int caloriesBurned;
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @OneToOne(mappedBy = "userProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;
}
