package app.rolly.backend.repository;

import app.rolly.backend.model.Role;
import app.rolly.backend.model.Route;
import app.rolly.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RouteRepositoryUnitTest {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Role role;
    private LocalDate date;
    private User user;
    private Route route;
    private Route savedRoute;
    private Duration duration;

    @BeforeEach
    void set(){
        role = new Role("testRole", "lorem ipsum");
        roleRepository.save(role);

        date = LocalDate.of(2000, 1, 1);

        user = new User(
                "testUsername",
                "test@test",
                "testPassword",
                date,
                role
        );
        userRepository.save(user);

        duration = Duration.ofMinutes(50);
        route = new Route(
                "testName",
                1.5,
                duration,
                user
        );

        savedRoute = routeRepository.save(route);
    }

    @Test
    void shouldSaveRoute(){
        // Given

        // When

        // Then
        assertNotNull(savedRoute.getId());
    }

    @Test
    void shouldFindRoute(){
        // Given

        // When
        Optional<Route> foundRoute = routeRepository.findById(savedRoute.getId());

        // Then
        assertTrue(foundRoute.isPresent());
        assertEquals("testName", foundRoute.get().getName());
        assertEquals(1.5, foundRoute.get().getDistance());
        assertEquals(duration, foundRoute.get().getEstimatedTime());
        assertEquals(user, foundRoute.get().getCreatedBy());
    }
}
