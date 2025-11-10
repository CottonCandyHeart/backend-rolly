package app.rolly.backend.service;

import app.rolly.backend.dto.TrainingPlanDto;
import app.rolly.backend.model.TrainingPlan;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;

    public List<TrainingPlanDto> getTrainingPlans(User user){
        try{
            return trainingPlanRepository.findByUser(user).stream()
                    .map(TrainingPlanDto::new)
                    .toList();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean addTrainingPlan(TrainingPlanDto trainingPlanDto, User user) {
        if (trainingPlanRepository.existsByDateTimeAndUser(trainingPlanDto.getDateTime(), user)) {
            return false;
        }

        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanDto, user);

        trainingPlanRepository.save(trainingPlan);
        return true;
    }

    public boolean modifyTrainingPlan(TrainingPlanDto trainingPlanDto, User user){
        Optional<TrainingPlan> isPresent;
        if ((isPresent = trainingPlanRepository.findById(trainingPlanDto.getId())).isEmpty()){
            return false;
        }

        TrainingPlan trainingPlan = isPresent.get();

        if (trainingPlan.getDateTime() != trainingPlanDto.getDateTime() &&
                trainingPlanRepository.existsByDateTimeAndUser(trainingPlanDto.getDateTime(), user)){
            return false;
        }

        trainingPlan.setDateTime(trainingPlanDto.getDateTime());
        trainingPlan.setTargetDuration(trainingPlanDto.getTargetDuration());
        trainingPlan.setNotes(trainingPlanDto.getNotes());

        return true;
    }

    public boolean removeTrainingPlan(Long id) {
        if ((trainingPlanRepository.findById(id)).isEmpty()){
            return false;
        }

        trainingPlanRepository.removeById(id);
        return true;
    }

    public boolean markAsCompleted(Long id, boolean bool){
        Optional<TrainingPlan> isPresent;
        if ((isPresent = trainingPlanRepository.findById(id)).isEmpty()){
            return false;
        }

        if (isPresent.get().isCompleted() == bool) return false;

        isPresent.get().setCompleted(bool);
        return true;
    }
}
