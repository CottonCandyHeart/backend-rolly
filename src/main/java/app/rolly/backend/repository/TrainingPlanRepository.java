package app.rolly.backend.repository;

import app.rolly.backend.dto.TrainingPlanDto;
import app.rolly.backend.model.TrainingPlan;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    boolean existsByDateTime(LocalDateTime dateTime);

    void removeById(Long id);

    List<TrainingPlan> findByUser(User user);

    boolean existsByDateTimeAndUser(LocalDateTime dateTime, User user);
}
