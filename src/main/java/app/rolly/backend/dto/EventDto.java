package app.rolly.backend.dto;

import app.rolly.backend.model.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private String name;
    private String description;
    private String organizerUsername;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private String level;
    private String type;
    private String age;
    private int numberOfParticipants;
    private String locationName;
    private String action = null;

    public EventDto(Event event){
        this.name = event.getName();
        this.description = event.getDescription();
        this.organizerUsername = event.getOrganizer().getUsername();
        this.date = event.getDate();
        this.time = event.getTime();
        this.level = event.getLevel();
        this.type = event.getType();
        this.age = event.getAge();
        this.numberOfParticipants = event.getNumOfParticipants();
        this.locationName = event.getLocation().getName();
    }

    public EventDto(Event event, String action){
        this.name = event.getName();
        this.description = event.getDescription();
        this.organizerUsername = event.getOrganizer().getUsername();
        this.date = event.getDate();
        this.time = event.getTime();
        this.level = event.getLevel();
        this.type = event.getType();
        this.age = event.getAge();
        this.numberOfParticipants = event.getNumOfParticipants();
        this.locationName = event.getLocation().getName();
        this.action = action;
    }
}
