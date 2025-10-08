package app.rolly.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String passwd;
    private int year;
    private int month;
    private int day;
    private String role;
}
