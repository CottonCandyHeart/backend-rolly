package app.rolly.backend.service;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.RoutePhoto;
import app.rolly.backend.model.RoutePoint;
import app.rolly.backend.model.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public boolean createRoute(RouteDto routeDto, String username){
        Optional<User> user = userRepository.findByUsername(username);
        Route route = new Route(routeDto, user.get());
        System.out.println(route.getName());
        System.out.println(route.getDistance());
        System.out.println(route.getEstimatedTime());
        System.out.println(route.getDate());
        System.out.println(route.getCreatedBy());
        routeRepository.save(route);
        return true;
    }




}
