package app.rolly.backend.service;

import app.rolly.backend.dto.CityDto;
import app.rolly.backend.dto.EventDto;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.CityRepository;
import app.rolly.backend.repository.EventRepository;
import app.rolly.backend.repository.LocationRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private EventService eventService;

    private User user;
    private Location location;
    private Event event;
    private Role role;

    @BeforeEach
    void set(){
        role = new Role("role", "lorem ipsum");
        user = new User(
                "user",
                "user@user",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                role
        );
        location = new Location(
                "location",
                "city",
                "country",
                0.0,
                1.1
        );
        event = new Event(
                "event",
                "lorem ipsum",
                user,
                LocalDate.of(2025,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                5,
                location
        );
    }

    @Test
    void shouldCreateEventSuccessfully(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(cityRepository.existsByCity("CITY")).thenReturn(true);

        // When
        boolean result = eventService.createEvent(eventDto, "user");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldSaveEventCityForNonExistingCityName(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(cityRepository.existsByCity("CITY")).thenReturn(false);

        // When
        boolean result = eventService.createEvent(eventDto, "user");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNonExistingUserWhileCreatingEvent(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));

        // When
        boolean result = eventService.createEvent(eventDto, "user");

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForNonExistingLocationWhileCreatingEvent(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(locationRepository.findByName("location")).thenReturn(Optional.empty());

        // When
        boolean result = eventService.createEvent(eventDto, "user");

        // Then
        assertFalse(result);
    }

    @Test
    void shouldJoinEventSuccessfully(){
        // Given
        EventDto eventDto = new EventDto(event);
        role = new Role("role", "lorem ipsum");
        User user2 = new User(
                "user2",
                "user@user",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                role
        );
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.of(event));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, "user2");
        int after = event.getAttendee().size();

        // Then
        assertTrue(result);
        assertEquals(before+1, after);
        assertTrue(event.getAttendee().contains(user2));
    }

    @Test
    void shouldReturnFalseForFullEvent(){
        // Given
        EventDto eventDto = new EventDto(event);

        for (int i=0; i<event.getNumOfParticipants(); i++){
            event.getAttendee().add(new User());
        }

        role = new Role("role", "lorem ipsum");
        User user2 = new User(
                "user2",
                "user@user",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                role
        );

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.of(event));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, "user2");
        int after = event.getAttendee().size();

        // Then
        assertFalse(result);
        assertEquals(before, after);
        assertFalse(event.getAttendee().contains(user2));
    }

    @Test
    void shouldReturnFalseFotOrganizerTryingToJoinEvent(){
        // Given
        EventDto eventDto = new EventDto(event);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, "user");
        int after = event.getAttendee().size();

        // Then
        assertFalse(result);
        assertEquals(before, after);
    }

    @Test
    void shouldThrowExceptionForNonExistingEventJoined(){
        // Given
        EventDto eventDto = new EventDto(event);

        role = new Role("role", "lorem ipsum");
        User user2 = new User(
                "user2",
                "user@user",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                role
        );

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(NotFoundException.class, ()->{
            eventService.joinEvent(eventDto, "user2");
        });
    }

    @Test
    void shouldReturnFalseIfUserIsAlreadyOnTheList(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );
        event.getAttendee().add(user1);

        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user1));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.of(event));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, "user2");
        int after = event.getAttendee().size();

        // Then
        assertFalse(result);
        assertEquals(before, after);
        assertTrue(event.getAttendee().contains(user1));
    }

    @Test
    void shouldLeaveEventSuccessfully(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );

        event.getAttendee().add(user1);

        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user1));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.of(event));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.leaveEvent(eventDto, "user2");
        int after = event.getAttendee().size();

        // Then
        assertTrue(result);
        assertEquals(before-1, after);
        assertFalse(event.getAttendee().contains(user));
    }

    @Test
    void shouldThrowExceptionForLeavingNonExistingEvent(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );

        event.getAttendee().add(user1);

        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user1));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(NotFoundException.class, ()->{
            eventService.leaveEvent(eventDto, "user2");
        });
    }

    @Test
    void shouldReturnFalseIfUserIsNotOnTheList(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );

        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user1));
        when(locationRepository.findByName("location")).thenReturn(Optional.of(location));
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(Optional.of(event));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.leaveEvent(eventDto, "user2");
        int after = event.getAttendee().size();

        // Then
        assertFalse(result);
        assertEquals(before, after);
        assertFalse(event.getAttendee().contains(user));
    }

    @Test
    void shouldReturnNumberOfParticipants(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );
        User user2 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );
        event.getAttendee().add(user1);
        event.getAttendee().add(user2);

        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        int result = eventService.getNumberOfParticipants("event");

        // Then
        assertEquals(2, result);
    }

    @Test
    void shouldThrowExceptionForNonExistingEvent(){
        // Given
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(NotFoundException.class, ()->{
            eventService.getNumberOfParticipants(event.getName());
        });
    }

    @Test
    void shouldReturnZeroForEmptyParticipantsList(){
        // Given
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        int result = eventService.getNumberOfParticipants(event.getName());

        // Then
        assertEquals(0, result);
    }

    @Test
    void shouldReturnEventDtoListForCity(){
        // Given
        Location location2 = new Location(
                "location",
                "city",
                "country",
                0.0,
                1.1
        );
        Event event2 = new Event(
                "event2",
                "event2 desc",
                user,
                LocalDate.of(2025,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                5,
                location2
        );

        when(eventRepository.findEventsByCity("CITY")).thenReturn(List.of(event,event2));

        // When
        List<EventDto> result = eventService.getEventsByCity("CITY");

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnZeroForEmptyEventDtoListForCity(){
        // Given

        // When
        List<EventDto> result = eventService.getEventsByCity("CITY");

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnEventDtoListForDate(){
        // Given
        Location location2 = new Location(
                "location",
                "city",
                "country",
                0.0,
                1.1
        );
        Event event2 = new Event(
                "event2",
                "event2 desc",
                user,
                LocalDate.of(2025,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                5,
                location2
        );

        when(eventRepository.findEventsByDate(LocalDate.of(2025,1,1))).thenReturn(List.of(event,event2));

        // When
        List<EventDto> result = eventService.getEventsByDate(LocalDate.of(2025,1,1));

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnZeroForEmptyEventDtoListForDate(){
        // Given

        // When
        List<EventDto> result = eventService.getEventsByDate(LocalDate.of(2025,1,1));

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllEventDtoList(){
        // Given
        Location location2 = new Location(
                "location",
                "city",
                "country",
                0.0,
                1.1
        );
        Event event2 = new Event(
                "event2",
                "event2 desc",
                user,
                LocalDate.of(2025,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                5,
                location2
        );

        when(eventRepository.findAll()).thenReturn(List.of(event, event2));

        // When
        List<EventDto> result = eventService.getEvents();

        // Then
        assertEquals(2, result.size());
        assertEquals(event.getName(), result.getFirst().getName());
        assertEquals(event2.getName(), result.getLast().getName());
    }

    @Test
    void shouldReturnEventDtoListForUpcomingEventsByCity(){
        // Given
        LocalDate now = LocalDate.now();
        event.setDate(LocalDate.of(now.getYear()+1,now.getMonth(),now.getDayOfMonth()));
        when(eventRepository.getUpcomingEventsByCity(event.getCity(), LocalDate.now())).thenReturn(List.of(event));

        // When
        List<EventDto> result = eventService.getUpcomingEventsByCity(event.getCity());

        // Then
        assertEquals(1, result.size());
        assertEquals(event.getName(), result.getFirst().getName());
    }

    @Test
    void shouldReturnEventDtoListByUser(){
        // Given
        user.getAttendedEvents().add(event);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // When
        List<EventDto> result = eventService.getEventsByUser(user.getUsername());

        // Then
        assertEquals(1, result.size());
        assertEquals(event.getName(), result.getFirst().getName());

    }

    @Test
    void shouldReturnCityDtoList(){
        // Given
        City city1 = new City("city1");
        City city2 = new City("city2");

        when(cityRepository.findAll()).thenReturn(List.of(city1, city2));

        // When
        Set<CityDto> result = eventService.getCities();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEventDtoListForOrganizer(){
        // Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(eventRepository.findEventsByOrganizer(user)).thenReturn(List.of(event));

        // When
        List<EventDto> result = eventService.getUserEvents(user.getUsername());

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseForDeletingNonExistingEvent(){
        // Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.empty());

        // When
        boolean result = eventService.deleteEvent(event.getName(), user.getUsername());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForDeletingNonExistingUser(){
        // Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.deleteEvent(event.getName(), user.getUsername());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseDeletingEventByNotOrganizer(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );

        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.deleteEvent(event.getName(), user1.getUsername());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldDeleteEventSuccessfully(){
        // Given
        event.getAttendee().add(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.deleteEvent(event.getName(), user.getUsername());
        int after = event.getAttendee().size();

        // Then
        assertTrue(result);
        assertEquals(before, after+1);
        assertFalse(event.getAttendee().contains(user));
    }

    @Test
    void shouldReturnTrueForOwner(){
        // Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.checkOwner(event.getName(), user.getUsername());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNotOwner(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.checkOwner(event.getName(), user1.getUsername());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueForAttendee(){
        // Given
        event.getAttendee().add(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.checkAttendee(event.getName(), user.getUsername());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForAttendee(){
        // Given
        User user1 = new User(
                "user2",
                "user2@user2",
                "hashedPasswd2",
                LocalDate.of(2001,2,2),
                role
        );
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(eventRepository.findEventsByName(event.getName())).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.checkAttendee(event.getName(), user1.getUsername());

        // Then
        assertFalse(result);
    }

}
