package app.rolly.backend.dto;

import app.rolly.backend.model.TrainingPlan;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
