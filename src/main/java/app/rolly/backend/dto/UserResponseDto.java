package app.rolly.backend.dto;

import app.rolly.backend.model.Achievement;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.UserProgress;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String email;
    private LocalDate birthday;
    private String role;
    private String level;

    private List<EventDto> organizedEvents;
    private Set<EventDto> attendedEvents;
    private Set<AchievementDto> achievements;
    private List<RouteDto> routesCreated;
    private UserProgressDto userProgress;
}
