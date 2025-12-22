package app.rolly.backend.dto;

import app.rolly.backend.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private String description;

    public RoleDto(Role role){
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }


}
