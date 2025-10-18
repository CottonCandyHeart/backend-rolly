package app.rolly.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminUserDto {
    private String username;
    private String email;
    private String role;
    private boolean active;
    private LocalDate createdAt;
}
