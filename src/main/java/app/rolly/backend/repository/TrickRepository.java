package app.rolly.backend.repository;

import app.rolly.backend.model.Trick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrickRepository extends JpaRepository<Trick, Long> {
}
