package app.rolly.backend.dto;

import app.rolly.backend.model.Achievement;
import app.rolly.backend.model.Event;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.UserProgress;
import app.rolly.backend.repository.UserRepository;
import app.rolly.backend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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

    public UserResponseDto(User user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.birthday = user.getDateOfBirth();
        this.role = user.getRole().getName();
        this.level = user.getUserLevel();
    }
}
