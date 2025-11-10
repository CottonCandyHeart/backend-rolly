package app.rolly.backend.dto;

import app.rolly.backend.model.TrainingPlan;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TrainingPlanDto {
    private Long id;
    private LocalDateTime dateTime;
    private int targetDuration;
    private String notes;
    private boolean completed;

    public TrainingPlanDto(TrainingPlan trainingPlan) {
        this.id = trainingPlan.getId();
        this.dateTime = trainingPlan.getDateTime();
        this.targetDuration = trainingPlan.getTargetDuration();
        this.notes = trainingPlan.getNotes();
        this.completed = trainingPlan.isCompleted();
    }
}
