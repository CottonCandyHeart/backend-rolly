package app.rolly.backend.controller;

import app.rolly.backend.dto.EventDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto){
        if (eventService.createEvent(eventDto)) {
            return new ResponseEntity<>("Event created", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed creating event", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/join-event")
    public ResponseEntity<?> joinEvent(@RequestBody EventDto eventDto, Authentication authentication){
        if (eventService.joinEvent(eventDto, (User) authentication.getPrincipal())) {
            return new ResponseEntity<>("Joined event", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed joining event", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/leave-event")
    public ResponseEntity<?> leaveEvent(@RequestBody EventDto eventDto, Authentication authentication){
        if (eventService.leaveEvent(eventDto, (User) authentication.getPrincipal())) {
            return new ResponseEntity<>("Left event", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed leaving event", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/num-of-part")
    public ResponseEntity<?> showNumberOfParticipants(EventDto eventDto){
        return new ResponseEntity<>(eventService.getNumberOfParticipants(eventDto), HttpStatus.OK);
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> showEventsByCity(@PathVariable String city){
        return new ResponseEntity<>(eventService.getEventsByCity(city), HttpStatus.OK);
    }

    @GetMapping("/{Y}-{m}-{d}")
    public ResponseEntity<?> showEventsByDate(@PathVariable int Y, @PathVariable int m, @PathVariable int d){
        return new ResponseEntity<>(eventService.getEventsByDate(LocalDate.of(Y,m,d)), HttpStatus.OK);
    }
}
