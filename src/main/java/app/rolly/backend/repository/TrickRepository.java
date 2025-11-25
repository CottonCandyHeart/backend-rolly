package app.rolly.backend.repository;

import app.rolly.backend.model.Trick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long> {
    List<Trick> findTricksByCategory_Name(String category_name);
    Optional<Trick> findByName(String name);
    List<Trick> findByUserProgresses_Id(Long userProgressId);
}
