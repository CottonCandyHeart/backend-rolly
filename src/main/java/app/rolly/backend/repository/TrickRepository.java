package app.rolly.backend.repository;

import app.rolly.backend.model.Trick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long> {
    List<Trick> findTricksByCategory_Name(String category_name);
    Trick findByName(String name);
}
