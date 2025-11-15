package app.rolly.backend.repository;


import app.rolly.backend.dto.EventDto;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventsByCity(String city);
    List<Event> findEventsByDate(LocalDate date);
    Optional<Event> findByOrganizerAndDateAndTimeAndLocation(
            User organizer,
            LocalDate date,
            LocalTime time,
            Location location
    );
}
