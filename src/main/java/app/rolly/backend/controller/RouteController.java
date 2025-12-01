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

    @GetMapping("/")
    public ResponseEntity<?> getUserRoutes(Authentication authentication){
        List<RouteDto> routeDtos = routeService.getUserRoute(authentication.getName());
        return new ResponseEntity<>(routeDtos, HttpStatus.OK);
    }

    @GetMapping("/get-by-date")
    public ResponseEntity<?> getUserRoutesByDate(@RequestBody LocalDate date, Authentication authentication){
        List<RouteDto> routeDtos = routeService.getUserRouteByDate(authentication.getName(), date);
        return new ResponseEntity<>(routeDtos, HttpStatus.OK);
    }

    @GetMapping("/m/{y}-{m}")
    public ResponseEntity<?> getTrainingsByYearAndMonth(@PathVariable int y, @PathVariable int m, Authentication authentication){
        List<RouteDto> routeDtos = routeService.getRoutesByYearAndMonth(authentication.getName(), y, m);
        return new ResponseEntity<>(routeDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoute(@RequestBody RouteDto routeDto, Authentication authentication){
        routeService.createRoute(routeDto, authentication.getName());
        return new ResponseEntity<>("Route created", HttpStatus.OK);
    }

}
