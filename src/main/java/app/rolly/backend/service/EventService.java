package app.rolly.backend.service;

import app.rolly.backend.dto.EventDto;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.EventRepository;
import app.rolly.backend.repository.LocationRepository;
import app.rolly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public boolean createEvent(EventDto eventDto){
        Optional<User> user = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());

        if (user.isEmpty() || location.isEmpty()) {
            return false;
        }

        Event event = new Event(
                user.get(),
                eventDto.getDate(),
                eventDto.getTime(),
                eventDto.getLevel(),
                eventDto.getType(),
                eventDto.getAge(),
                eventDto.getNumberOfParticipants(),
                location.get()
        );
        eventRepository.save(event);

        return true;
    }

    public boolean joinEvent(EventDto eventDto, String username){
        User user = userRepository.findByUsername(username).get();
        Optional<Event> event;
        Optional<User> organizer = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                    organizer.get(),
                    eventDto.getDate(),
                    eventDto.getTime(),
                    location.get()
        );

        if (event.isEmpty()) throw new NotFoundException("Event");

        if (event.get().getAttendee().size() < event.get().getNumOfParticipants()) {
            if (event.get().getAttendee().contains(user)){
                return false;
            }
            event.get().getAttendee().add(user);
        } else {
            return false;
        }

        return true;
    }

    public boolean leaveEvent(EventDto eventDto, String username){
        User user = userRepository.findByUsername(username).get();
        Optional<Event> event;
        Optional<User> organizer = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                organizer.get(),
                eventDto.getDate(),
                eventDto.getTime(),
                location.get()
        );

        if (event.isEmpty()) throw new NotFoundException("Event");

        if (event.get().getAttendee().contains(user)) {
            event.get().getAttendee().remove(user);
        } else {
            return false;
        }

        return true;
    }

    public int getNumberOfParticipants(EventDto eventDto){
        Optional<Event> event;
        Optional<User> user = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user.get(),
                eventDto.getDate(),
                eventDto.getTime(),
                location.get()
        );

        if (event.isEmpty()) throw new NotFoundException("Event");

        return event.get().getAttendee().size();
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
