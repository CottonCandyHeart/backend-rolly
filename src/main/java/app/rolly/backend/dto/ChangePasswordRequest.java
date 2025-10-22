package app.rolly.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {
    private String currentPasswd;
    private String newPasswd;
}
