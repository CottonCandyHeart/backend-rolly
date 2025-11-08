package app.rolly.backend.dto;

import app.rolly.backend.model.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventDto {
    private String organizerUsername;
    private LocalDate date;
    private LocalTime time;
    private String level;
    private String type;
    private String age;
    private int numberOfParticipants;
    private String locationName;
    private String action = null;

    public EventDto(Event event){
        this.organizerUsername = event.getOrganizer().getUsername();
        this.date = event.getDate();
        this.time = event.getTime();
        this.level = event.getLevel();
        this.age = event.getAge();
        this.numberOfParticipants = event.getNumOfParticipants();
        this.locationName = event.getLocation().getName();
    }

    public EventDto(Event event, String action){
        this.organizerUsername = event.getOrganizer().getUsername();
        this.date = event.getDate();
        this.time = event.getTime();
        this.level = event.getLevel();
        this.age = event.getAge();
        this.numberOfParticipants = event.getNumOfParticipants();
        this.locationName = event.getLocation().getName();
        this.action = action;
    }
}
