package app.rolly.backend.service;

import app.rolly.backend.dto.CityDto;
import app.rolly.backend.dto.EventDto;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.model.City;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.CityRepository;
import app.rolly.backend.repository.EventRepository;
import app.rolly.backend.repository.LocationRepository;
import app.rolly.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;

    public boolean createEvent(EventDto eventDto, String username){
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());

        if (user.isEmpty() || location.isEmpty()) {
            return false;
        }

        Event event = new Event(
                eventDto.getName(),
                eventDto.getDescription(),
                user.get(),
                eventDto.getDate(),
                eventDto.getTime(),
                eventDto.getLevel(),
                eventDto.getType(),
                eventDto.getAge(),
                eventDto.getNumberOfParticipants(),
                location.get()
        );

        event.getAttendee().add(user.get());
        user.get().getAttendedEvents().add(event);

        eventRepository.save(event);
        userRepository.save(user.get());

        if (!cityRepository.existsByCity(location.get().getCity().toUpperCase()))
            cityRepository.save(new City(location.get().getCity()));

        return true;
    }

    @Transactional
    public boolean joinEvent(EventDto eventDto, String username){
        Optional<Event> event;
        Optional<User> organizer = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());

        if (user.get().getUsername().equals(organizer.get().getUsername())) return false;

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                    organizer.get(),
                    eventDto.getDate(),
                    eventDto.getTime(),
                    location.get()
        );

        if (event.isEmpty()) throw new NotFoundException("Event");

        if (event.get().getAttendee().size() < event.get().getNumOfParticipants()) {
            if (event.get().getAttendee().contains(user.get())){
                return false;
            }
            user.get().getAttendedEvents().add(event.get());
            userRepository.save(user.get());
            event.get().getAttendee().add(user.get());
            eventRepository.save(event.get());

            return true;
        }

        return false;
    }

    @Transactional
    public boolean leaveEvent(EventDto eventDto, String username){
        System.out.println("Entered leaveEvent");
        User user = userRepository.findByUsername(username).get();
        Optional<Event> event;
        Optional<User> organizer = userRepository.findByUsername(eventDto.getOrganizerUsername());
        Optional<Location> location = locationRepository.findByName(eventDto.getLocationName());
        System.out.println("Found objects");

        event = eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                organizer.get(),
                eventDto.getDate(),
                eventDto.getTime(),
                location.get()
        );
        System.out.println("Found event");

        if (event.isEmpty()) throw new NotFoundException("Event");

        if (event.get().getAttendee().contains(user)) {
            System.out.println("Entered if");
            event.get().getAttendee().remove(user);
            user.getAttendedEvents().remove(event.get());

            userRepository.save(user);
            eventRepository.save(event.get());
        } else {
            return false;
        }

        return true;
    }

    public int getNumberOfParticipants(String name){
        Optional<Event> event = eventRepository.findEventsByName(name);

        if (event.isEmpty()) throw new NotFoundException("Event");

        return event.get().getAttendee().size();
    }

    public List<EventDto> getEventsByCity(String city){
        return eventRepository.findEventsByCity(city).stream()
                .map(EventDto::new)
                .toList();
    }

    public List<EventDto> getEvents(){
        return eventRepository.findAll().stream()
                .map(EventDto::new)
                .toList();
    }

    public List<EventDto> getUpcomingEventsByCity(String city){
        List<Event> events = eventRepository.getUpcomingEventsByCity(city.toUpperCase(), LocalDate.now());
        System.out.println(events.size());
        return events.stream()
                .map(EventDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsByUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.get().getAttendedEvents().stream()
                .map(EventDto::new)
                .toList();
    }

    public List<EventDto> getEventsByDate(LocalDate date){
        return eventRepository.findEventsByDate(date).stream()
                .map(EventDto::new)
                .toList();
    }

    public Set<CityDto> getCities(){
        return cityRepository.findAll().stream()
                .map(CityDto::new)
                .collect(Collectors.toSet());
    }

    public List<EventDto> getUserEvents(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return eventRepository.findEventsByOrganizer(user.get()).stream()
                .map(EventDto::new)
                .toList();
    }

    @Transactional
    public boolean deleteEvent(String name, String username){
        Optional<Event> eventOpt = eventRepository.findEventsByName(name);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (eventOpt.isEmpty() || userOpt.isEmpty()) {
            return false;
        }

        Event event = eventOpt.get();
        User user = userOpt.get();

        if (!event.getOrganizer().getUsername().equals(user.getUsername())){
            return false;
        }

        Set<User> att = new HashSet<>(event.getAttendee());
        for (User u : att){
            u.getAttendedEvents().remove(event);
            userRepository.save(u);
        }

        event.getAttendee().clear();

        eventRepository.removeEventByName(name);
        return true;
    }

    public boolean checkOwner(String name, String username){
        Optional<Event> event = eventRepository.findEventsByName(name);
        Optional<User> organizer = userRepository.findByUsername(event.get().getOrganizer().getUsername());
        Optional<User> user = userRepository.findByUsername(username);

        System.out.println(user.get().getUsername().equals(organizer.get().getUsername()));

        if (user.get().getUsername().equals(organizer.get().getUsername())) return true;

        return false;
    }

    public boolean checkAttendee(String name, String username){
        Optional<Event> event = eventRepository.findEventsByName(name);
        Optional<User> user = userRepository.findByUsername(username);

        if (event.get().getAttendee().contains(user.get())) return true;

        return false;
    }
}
