package app.rolly.backend.service;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.dto.TrainingPlanDto;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.RouteRepository;
import app.rolly.backend.repository.UserRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    public List<RouteDto> getUserRoute(String username){
        try{
            User user = userRepository.findByUsername(username).get();
            return routeRepository.getRouteByCreatedBy(user).stream()
                    .map(RouteDto::new)
                    .toList();
        } catch (Exception e) {
            return null;
        }

    }

    public List<RouteDto> getUserRouteByDate(String username, LocalDate date){
        try{
            User user = userRepository.findByUsername(username).get();
            return routeRepository.getRouteByCreatedByAndDate(user, date).stream()
                    .map(RouteDto::new)
                    .toList();
        } catch (Exception e){
            return null;
        }
    }

    public List<RouteDto> getRoutesByYearAndMonth(String username, int year, int month){
        LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);

        List<Route> routes = routeRepository.findRoutesByUserAndMonth(username, start, end);

        return routes.stream()
                .map(RouteDto::new)
                .collect(Collectors.toList());
    }

    public boolean createRoute(RouteDto routeDto, String username){
        Optional<User> user = userRepository.findByUsername(username);
        Route route = new Route(routeDto, user.get());
        System.out.println(route.getDate());
        System.out.println(route.getCreatedBy());

        UserProgress userProgress = user.get().getUserProgress();
        userProgress.setTotalDistance(userProgress.getTotalDistance() + route.getDistance());
        userProgress.setTotalSessions(userProgress.getTotalSessions()+1);
        userProgress.setCaloriesBurned(userProgress.getCaloriesBurned() + route.getCaloriesBurned());
        userProgress.setLastUpdated(LocalDateTime.now());

        routeRepository.save(route);
        return true;
    }

}
