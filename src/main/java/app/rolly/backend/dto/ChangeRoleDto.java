package app.rolly.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChangeRoleDto {
    private String username;
    private String roleName;
}
