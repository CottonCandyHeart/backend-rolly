package app.rolly.backend.repository;

import app.rolly.backend.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("""
        SELECT a FROM Achievement a
            WHERE a.requiredDistance <= :dist
                OR a.requiredSessionsCount <= :sess
                OR a.requiredTricksLearnedCount <= :trick
                OR a.requiredCaloriesBurned <= :cal
    """)
    Set<Achievement> findUserAchievements(
            @Param("dist") double totalDistance,
            @Param ("sess") int totalSessions,
            @Param("trick") int totalTricksLearned,
            @Param("cal") int caloriesBurned
    );
}
