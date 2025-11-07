package app.rolly.backend.repository;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> getRouteByCreatedBy(User user);

    List<Route> getRouteByCreatedByAndDate(User createdBy, LocalDate date);
}
