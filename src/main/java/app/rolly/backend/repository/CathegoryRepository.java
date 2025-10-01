package app.rolly.backend.repository;

import app.rolly.backend.model.Cathegory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CathegoryRepository extends JpaRepository<Cathegory, Long> {
}
