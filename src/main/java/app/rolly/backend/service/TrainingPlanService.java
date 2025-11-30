package app.rolly.backend.service;

import app.rolly.backend.dto.TrainingPlanDto;
import app.rolly.backend.model.TrainingPlan;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.TrainingPlanRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;

    public List<TrainingPlanDto> getTrainingPlans(String username){
        try{
            User user = userRepository.findByUsername(username).get();
            return trainingPlanRepository.findByUser(user).stream()
                    .map(TrainingPlanDto::new)
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrainingPlanDto> getTrainingPlansByDate(String username, int year, int month, int day){
        try{
            User user = userRepository.findByUsername(username).get();
            return trainingPlanRepository.findByUser(user).stream()
                    .filter(tp -> tp.getDateTime().getYear() == year &&
                            tp.getDateTime().getMonthValue() == month && tp.getDateTime().getDayOfMonth() == day)
                    .map(TrainingPlanDto::new)
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrainingPlanDto> getTrainingPlansByYearAndMonth(String username, int year, int month){
        try{
            User user = userRepository.findByUsername(username).get();
            return trainingPlanRepository.findByUser(user).stream()
                    .filter(tp -> tp.getDateTime().getYear() == year &&
                            tp.getDateTime().getMonthValue() == month)
                    .map(TrainingPlanDto::new)
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean addTrainingPlan(TrainingPlanDto trainingPlanDto, String username) {
        User user = userRepository.findByUsername(username).get();
        if (trainingPlanRepository.existsByDateTimeAndUser(trainingPlanDto.getDateTime(), user)) {
            return false;
        }

        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanDto, user);

        trainingPlanRepository.save(trainingPlan);
        return true;
    }

    public boolean modifyTrainingPlan(TrainingPlanDto trainingPlanDto, String username){
        User user = userRepository.findByUsername(username).get();
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
