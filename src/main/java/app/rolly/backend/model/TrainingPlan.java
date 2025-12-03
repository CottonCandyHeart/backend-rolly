package app.rolly.backend.model;

import app.rolly.backend.dto.TrainingPlanDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="training-plan")
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TrainingPlan(LocalDateTime dateTime, int targetDuration, String notes, boolean completed, User user){
        this.dateTime = dateTime;
        this.targetDuration = targetDuration;
        this.notes = notes;
        this.completed = completed;
        this.user = user;
    }

    public TrainingPlan(TrainingPlanDto trainingPlanDto, User user){
        this.dateTime = trainingPlanDto.getDateTime();
        this.targetDuration = trainingPlanDto.getTargetDuration();
        this.notes = trainingPlanDto.getNotes();
        this.completed = trainingPlanDto.isCompleted();
        this.user = user;
    }

    @Column(nullable = false)
    private LocalDateTime dateTime;
    @Column(nullable = false)
    private int targetDuration;  // in minutes
    @Column(nullable = false)
    private String notes;
    @Column(nullable = false)
    private boolean completed;

    @ManyToOne
    private User user;
}
