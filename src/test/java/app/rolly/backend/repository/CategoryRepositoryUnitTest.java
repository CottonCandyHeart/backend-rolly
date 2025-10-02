package app.rolly.backend.repository;

import app.rolly.backend.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryUnitTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Category savedCategory;

    @BeforeEach
    void set(){
        category = new Category("testCategory");
        savedCategory = categoryRepository.save(category);
    }

    @Test
    void shouldSaveCategory(){
        // Given

        // When

        // Then
        assertNotNull(savedCategory.getId());
    }

    @Test
    void shouldFindCategory(){
        // Given

        // When
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        // Then
        assertTrue(foundCategory.isPresent());
        assertEquals("testCategory", foundCategory.get().getName());
    }
}
