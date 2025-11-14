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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    public List<RouteDto> getUserRoute(User user){
        try{
            return routeRepository.getRouteByCreatedBy(user).stream()
                    .map(RouteDto::new)
                    .toList();
        } catch (Exception e) {
            return null;
        }

    }

    public List<RouteDto> getUserRouteByDate(User user, LocalDate date){
        try{
            return routeRepository.getRouteByCreatedByAndDate(user, date).stream()
                    .map(RouteDto::new)
                    .toList();
        } catch (Exception e){
            return null;
        }

    }

    public boolean createRoute(RouteDto routeDto){
        Optional<User> user = userRepository.findByUsername(routeDto.getCreatedBy());
        Route route = new Route(routeDto, user.get());
        routeRepository.save(route);
        return true;
    }




}
