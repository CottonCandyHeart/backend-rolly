package app.rolly.backend.service;

import app.rolly.backend.dto.EventDto;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Location;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.EventRepository;
import app.rolly.backend.repository.LocationRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    LocationRepository locationRepository;

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
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);

        // When
        boolean result = eventService.createEvent(eventDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForNonExistingUserWhileCreatingEvent(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(null);
        when(locationRepository.findByName("location")).thenReturn(location);

        // When
        boolean result = eventService.createEvent(eventDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForNonExistingLocationWhileCreatingEvent(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(null);

        // When
        boolean result = eventService.createEvent(eventDto);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldJoinEventSuccessfully(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, user);
        int after = event.getAttendee().size();

        // Then
        assertTrue(result);
        assertEquals(before+1, after);
        assertTrue(event.getAttendee().contains(user));
    }

    @Test
    void shouldReturnFalseForFullEvent(){
        // Given
        EventDto eventDto = new EventDto(event);

        for (int i=0; i<event.getNumOfParticipants(); i++){
            event.getAttendee().add(new User());
        }

        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, user);
        int after = event.getAttendee().size();

        // Then
        assertFalse(result);
        assertEquals(before, after);
        assertFalse(event.getAttendee().contains(user));
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
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.joinEvent(eventDto, user1);
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
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.leaveEvent(eventDto, user1);
        int after = event.getAttendee().size();

        // Then
        assertTrue(result);
        assertEquals(before-1, after);
        assertFalse(event.getAttendee().contains(user));
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
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int before = event.getAttendee().size();
        boolean result = eventService.leaveEvent(eventDto, user1);
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

        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int result = eventService.getNumberOfParticipants(eventDto);

        // Then
        assertEquals(2, result);
    }

    @Test
    void shouldReturnZeroForEmptyParticipantsList(){
        // Given
        EventDto eventDto = new EventDto(event);
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(locationRepository.findByName("location")).thenReturn(location);
        when(eventRepository.findByOrganizerAndDateAndTimeAndLocation(
                user,
                event.getDate(),
                event.getTime(),
                location
        )).thenReturn(event);

        // When
        int result = eventService.getNumberOfParticipants(eventDto);

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
                user,
                LocalDate.of(2025,1,1),
                LocalTime.of(1,1,1),
                "level",
                "type",
                "age",
                5,
                location2
        );

        when(eventRepository.findEventsByCity("city")).thenReturn(List.of(event,event2));

        // When
        List<EventDto> result = eventService.getEventsByCity("city");

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnZeroForEmptyEventDtoListForCity(){
        // Given

        // When
        List<EventDto> result = eventService.getEventsByCity("city");

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

}
