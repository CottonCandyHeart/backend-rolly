package app.rolly.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RouteDto {
    private String name;
    private double distance;
    private Duration estimatedTime;
    private String username;
}
