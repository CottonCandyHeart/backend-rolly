package app.rolly.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EventRepositoryUnitTest {
    @Autowired
    private EventRepository eventRepository;
}
