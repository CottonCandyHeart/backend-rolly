package app.rolly.backend.repository;


import app.rolly.backend.dto.EventDto;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT e FROM Event e JOIN FETCH e.organizer JOIN FETCH e.location WHERE UPPER(e.city) = UPPER(:city) AND e.date >= :date ORDER BY e.date ASC, e.time ASC")
    List<Event> findByCityAndDateGreaterThanEqualOrderByDateAscTimeAsc(String city, LocalDate date);

    @Query("SELECT e FROM Event e JOIN FETCH e.organizer JOIN FETCH e.location WHERE e.city = :city AND e.date >= :date ORDER BY e.date ASC, e.time ASC")
    List<Event> getUpcomingEventsByCity(@Param("city") String city, @Param("date") LocalDate date);

    List<Event> findEventsByOrganizer(User organizer);

    Optional<Event> findEventsByName(String name);

    List<Event> readEventByName(String name);

    void removeEventByName(String name);

}
