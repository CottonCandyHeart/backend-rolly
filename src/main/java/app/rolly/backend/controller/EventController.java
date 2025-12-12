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

    @PostMapping("/")
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto, Authentication authentication){

        if (eventDto.getAction().equals("create")){
            if (eventService.createEvent(eventDto, authentication.getName())) {
                return new ResponseEntity<>("Event created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed creating event", HttpStatus.BAD_REQUEST);
            }
        } else if (eventDto.getAction().equals("join")){
            if (eventService.joinEvent(eventDto, authentication.getName())) {
                return new ResponseEntity<>("Joined event", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed joining event", HttpStatus.BAD_REQUEST);
            }
        } else if (eventDto.getAction().equals("leave")) {
            if (eventService.leaveEvent(eventDto, authentication.getName())) {
                return new ResponseEntity<>("Left event", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed leaving event", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Action undefined", HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/participants/{name}")
    public ResponseEntity<?> showNumberOfParticipants(@PathVariable String name){
        return new ResponseEntity<>(eventService.getNumberOfParticipants(name), HttpStatus.OK);
    }

    @GetMapping("/c")
    public ResponseEntity<?> getCities(){
        return new ResponseEntity<>(eventService.getCities(), HttpStatus.OK);
    }

    @GetMapping("/c/{city}")
    public ResponseEntity<?> showEventsByCity(@PathVariable String city){
        return new ResponseEntity<>(eventService.getEventsByCity(city), HttpStatus.OK);
    }

    @GetMapping("/c/{city}/up")
    public ResponseEntity<?> showUpcomingsEventsByCity(@PathVariable String city){
        return new ResponseEntity<>(eventService.getUpcomingEventsByCity(city), HttpStatus.OK);
    }

    @GetMapping("/d/{Y}-{m}-{d}")
    public ResponseEntity<?> showEventsByDate(@PathVariable int Y, @PathVariable int m, @PathVariable int d){
        return new ResponseEntity<>(eventService.getEventsByDate(LocalDate.of(Y,m,d)), HttpStatus.OK);
    }

    @GetMapping("/u")
    public ResponseEntity<?> showUserEvents(Authentication authentication){
        return new ResponseEntity<>(eventService.getUserEvents(authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/del/{name}")
    public ResponseEntity<?> deleteEvent(@PathVariable String name, Authentication authentication){
        boolean deleted = eventService.deleteEvent(name, authentication.getName());

        if (deleted) {
            return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot delete event", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/check/{name}")
    public ResponseEntity<?> checkOwner(@PathVariable String name, Authentication authentication){
        return new ResponseEntity<>(eventService.checkOwner(name, authentication.getName()), HttpStatus.OK);
    }
}
