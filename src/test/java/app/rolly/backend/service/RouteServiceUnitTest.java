package app.rolly.backend.service;

import app.rolly.backend.dto.RouteDto;
import app.rolly.backend.model.Role;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.User;
import app.rolly.backend.repository.RouteRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceUnitTest {
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RouteService routeService;

    private Route route;
    private User user;

    @BeforeEach
    void set(){
        user = new User(
                "username",
                "email",
                "hashedPasswd",
                LocalDate.of(2000,1,1),
                new Role("role", "role")
        );
        route = new Route(
                "testRoute",
                0.0,
                Duration.ofHours(1),
                user
        );
        route.setDate(LocalDate.of(2025,1,1));
    }

    @Test
    void shouldReturnUserRouteDtoList(){
        // Given
        Route route2 = new Route(
                "testRoute2",
                2.2,
                Duration.ofHours(2),
                user
        );
        route2.setDate(LocalDate.of(2025,2,2));
        when(routeRepository.getRouteByCreatedBy(user)).thenReturn(List.of(route, route2));

        // When
        List<RouteDto> routeDtos = routeService.getUserRoute(user);

        // Then
        assertNotNull(routeDtos);
        assertEquals(2, routeDtos.size());
    }

    @Test
    void shouldReturnEmptyUserRouteDtoList(){
        // Given
        when(routeRepository.getRouteByCreatedBy(user)).thenReturn(null);

        // When
        List<RouteDto> routeDtos = routeService.getUserRoute(user);

        // Then
        assertNull(routeDtos);
    }

    @Test
    void shouldReturnUserRouteDtoListByDate(){
        // Given
        Route route2 = new Route(
                "testRoute2",
                2.2,
                Duration.ofHours(2),
                user
        );
        route2.setDate(LocalDate.of(2025,2,2));
        when(
                routeRepository.getRouteByCreatedByAndDate(
                        user,
                        LocalDate.of(2025, 2,2))
        ).thenReturn(List.of(route2));

        // When
        List<RouteDto> routeDtos = routeService.getUserRouteByDate(
                user,
                LocalDate.of(2025, 2,2)
        );

        // Then
        assertNotNull(routeDtos);
        assertEquals(1, routeDtos.size());
    }

    @Test
    void shouldReturnEmptyUserRouteDtoListByDate(){
        // Given
        when(
                routeRepository.getRouteByCreatedByAndDate(
                        user,
                        LocalDate.of(2025, 2,2))
        ).thenReturn(null);

        // When
        List<RouteDto> routeDtos = routeService.getUserRouteByDate(
                user,
                LocalDate.of(2025, 2,2)
        );

        // Then
        assertNull(routeDtos);
    }

    @Test
    void shouldCreateRouteSuccessfully(){
        // Given
        RouteDto routeDto = new RouteDto(route);
        when(userRepository.findByUsername("username")).thenReturn(user);

        // When
        boolean result = routeService.createRoute(routeDto);

        // Then
        assertTrue(result);
    }


}
