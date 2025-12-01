package app.rolly.backend.repository;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> getRouteByCreatedBy(User user);

    List<Route> getRouteByCreatedByAndDate(User createdBy, LocalDate date);

    @Query("""
        SELECT r FROM Route r
        WHERE r.createdBy.username = :username
          AND r.date >= :startDate
          AND r.date < :endDate
    """)
    List<Route> findRoutesByUserAndMonth(
            @Param("username") String username,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
