package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "user_progress_seq")
    @SequenceGenerator(
            name = "user_progress_seq",
            sequenceName = "user_progress_sequence",
            allocationSize = 1
    )
    private long id;

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
