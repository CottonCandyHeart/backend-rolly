package app.rolly.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CathegoryRepositoryUnitTest {
    @Autowired
    private CathegoryRepository cathegoryRepository;
}
