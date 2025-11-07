package app.rolly.backend.controller;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/route")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/get")
    public ResponseEntity<?> getUserRoutes(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        List<RouteDto> routeDtos = routeService.getUserRoute(user);
        return new ResponseEntity<>(routeDtos, HttpStatus.OK);
    }

    @GetMapping("/get-by-date")
    public ResponseEntity<?> getUserRoutesByDate(@RequestBody LocalDate date, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        List<RouteDto> routeDtos = routeService.getUserRouteByDate(user, date);
        return new ResponseEntity<>(routeDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoute(@RequestBody RouteDto routeDto){
        routeService.createRoute(routeDto);
        return new ResponseEntity<>("Route created", HttpStatus.OK);
    }

}
