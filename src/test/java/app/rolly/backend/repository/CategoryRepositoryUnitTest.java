package app.rolly.backend.repository;

import app.rolly.backend.model.Category;
import app.rolly.backend.model.Trick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryUnitTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TrickRepository trickRepository;

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

    @Test
    void shouldReturnNullForNonExistingCategory() {
        // Given

        // When
        Optional<Category> foundCategory = categoryRepository.findById(-1L);

        // Then
        assertTrue(foundCategory.isEmpty());
    }

    @Test
    void shouldFindAttachedTricks(){
        // Given
        Trick trick1 = new Trick(
                category,
                "test1",
                "test1Link",
                "lorem ipsum 1"
        );
        Trick trick2 = new Trick(
                category,
                "test2",
                "test2Link",
                "lorem ipsum 2"
        );
        category.getTricks().add(trick1);
        category.getTricks().add(trick2);

        trickRepository.save(trick1);
        trickRepository.save(trick2);
        categoryRepository.save(category);

        // When
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        List<Trick> tricks = foundCategory.get().getTricks();

        // Then
        assertFalse(tricks.isEmpty());
        assertEquals(2, tricks.size());
        assertTrue(tricks.contains(trick1));
        assertTrue(tricks.contains(trick2));
    }

    @Test
    void shouldReturnEmptyTrickList(){
        // Given

        // When
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        List<Trick> tricks = foundCategory.get().getTricks();

        // Then
        assertTrue(tricks.isEmpty());
    }
}
