package app.rolly.backend.dto;

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
    private String city;
    private String level;
    private String type;
    private String age;
    private int numberOfParticipants;
    private String locationName;
}
