package app.rolly.backend.service;

import app.rolly.backend.dto.EventDto;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.EventRepository;
import app.rolly.backend.repository.LocationRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final UserDetailsPasswordService userDetailsPasswordService;

    public boolean createEvent(EventDto eventDto){
        User user = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Location location = locationRepository.findByName(eventDto.getLocationName());

        if (user == null || location == null) {
            return false;
        }

        Event event = new Event(
                user,
                eventDto.getDate(),
                eventDto.getTime(),
                eventDto.getLevel(),
                eventDto.getType(),
                eventDto.getAge(),
                eventDto.getNumberOfParticipants(),
                location
        );
        eventRepository.save(event);

        return true;
    }

    public boolean joinEvent(EventDto eventDto, User user){
        Event event;
        User organizer = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Location location = locationRepository.findByName(eventDto.getLocationName());

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                    organizer,
                    eventDto.getDate(),
                    eventDto.getTime(),
                    location
        );

        if (event.getAttendee().size() < event.getNumOfParticipants()) {
            if (event.getAttendee().contains(user)){
                return false;
            }
            event.getAttendee().add(user);
        } else {
            return false;
        }

        return true;
    }

    public boolean leaveEvent(EventDto eventDto, User user){
        Event event;
        User organizer = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Location location = locationRepository.findByName(eventDto.getLocationName());

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                organizer,
                eventDto.getDate(),
                eventDto.getTime(),
                location
        );

        if (event.getAttendee().contains(user)) {
            event.getAttendee().remove(user);
        } else {
            return false;
        }

        return true;
    }

    public int getNumberOfParticipants(EventDto eventDto){
        Event event;
        User user = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Location location = locationRepository.findByName(eventDto.getLocationName());

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                eventDto.getDate(),
                eventDto.getTime(),
                location
        );

        return event.getAttendee().size();
    }

    public List<EventDto> getEventsByCity(String city){
        return eventRepository.findEventsByCity(city).stream()
                .map(EventDto::new)
                .toList();
    }

    public List<EventDto> getEventsByDate(LocalDate date){
        return eventRepository.findEventsByDate(date).stream()
                .map(EventDto::new)
                .toList();
    }

}
