package app.rolly.backend.service;

import app.rolly.backend.dto.CategoryDto;
import app.rolly.backend.dto.CategoryProgressDto;
import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.exception.CategoryAlreadyExistsException;
import app.rolly.backend.exception.NotFoundException;
import app.rolly.backend.exception.TrickAlreadyExistsException;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.CategoryRepository;
import app.rolly.backend.repository.TrickRepository;
import app.rolly.backend.repository.UserProgressRepository;
import app.rolly.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrickServiceUnitTest {
    @Mock
    private TrickRepository trickRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserProgressRepository userProgressRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TrickService trickService;

    private Category category;
    private Trick trick;
    private UserProgress userProgress;
    private User user;

    @BeforeEach
    void set() {
        category = new Category("testCategory");

        trick = new Trick(
                category,
                "testTrick",
                "testLink",
                "R",
                "lorem ipsum"
        );

        user = new User(
                "user",
                "user@user",
                "hashedPasswd",
                LocalDate.of(2000, 2, 2),
                new Role(
                        "role",
                        "lorem ipsum"
                )
        );

        userProgress = new UserProgress(
                1.0,
                2,
                3,
                4,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                user
        );

        user.setUserProgress(userProgress);
        trick.getUserProgresses().add(userProgress);
        userProgress.getMasteredTricks().add(trick);
    }

    @Test
    void shouldReturnTrickDto() {
        // Given
        when(trickRepository.findByName("testTrick")).thenReturn(Optional.of(trick));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        TrickDto trickDto = trickService.getTrick("testTrick", "user");

        // Then
        assertNotNull(trickDto);
        assertEquals(trick.getCategory().getName(), trickDto.getCategoryName());
        assertEquals(trick.getName(), trickDto.getTrickName());
        assertEquals(trick.getLink(), trickDto.getLink());
        assertEquals(trick.getLeg(), trickDto.getLeg());
        assertEquals(trick.getDescription(), trickDto.getDescription());
    }

    @Test
    void shouldThrowExceptionForNonExistingTrick() {
        // Given
        when(trickRepository.findByName("wrongTrick")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> {
            trickService.getTrick("wrongTrick", "user");
        });
    }

    @Test
    void shouldReturnListOfTrickDtoForCategory() {
        // Given
        Trick trick2 = new Trick(
                category,
                "testTrick2",
                "testLink2",
                "L",
                "lorem ipsum2"
        );

        when(trickRepository.findTricksByCategory_Name("testCategory")).thenReturn(List.of(trick, trick2));
        when(categoryRepository.findByName("testCategory")).thenReturn(Optional.of(category));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        List<TrickDto> tricks = trickService.getTricksByCategory("testCategory", "user");

        // Then
        assertEquals(2, tricks.size());
        assertEquals(trick.getName(), tricks.getFirst().getTrickName());
        assertEquals(trick2.getName(), tricks.getLast().getTrickName());
        assertEquals("testCategory", tricks.getFirst().getCategoryName());
        assertEquals("testCategory", tricks.getLast().getCategoryName());
    }

    @Test
    void shouldReturnEmptyTrickListForCategory() {
        // Given
        when(trickRepository.findTricksByCategory_Name("testCategory")).thenReturn(null);
        when(categoryRepository.findByName("testCategory")).thenReturn(Optional.of(category));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        List<TrickDto> tricks = trickService.getTricksByCategory("testCategory", "user");

        // Then
        assertEquals(0, tricks.size());
    }

    @Test
    void shouldThrowExceptionForNonExistingCategory() {
        // Given
        when(categoryRepository.findByName("wrongCategory")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> {
            trickService.getTricksByCategory("wrongCategory", "user");
        });
    }

    @Test
    void shouldSetTrickAsMastered() {
        // Given
        Trick trick2 = new Trick(
                category,
                "testTrick2",
                "testLink2",
                "L",
                "lorem ipsum2"
        );
        when(trickRepository.findByName("testTrick2")).thenReturn(Optional.of(trick2));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        trickService.setTrickAsMastered("testTrick2", "user");

        // Then
        assertTrue(userProgress.getMasteredTricks().contains(trick2));
    }

    @Test
    void shouldSetTrickAsMasteredWhenUserProgressIsNull() {
        // Given
        user.setUserProgress(null);
        Trick trick2 = new Trick(
                category,
                "testTrick2",
                "testLink2",
                "L",
                "lorem ipsum2"
        );
        when(trickRepository.findByName("testTrick2")).thenReturn(Optional.of(trick2));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        trickService.setTrickAsMastered("testTrick2", "user");
        UserProgress updatedProgress = user.getUserProgress();

        // Then
        assertNotNull(updatedProgress);
        assertTrue(updatedProgress.getMasteredTricks().contains(trick2));
        assertEquals(1, updatedProgress.getTotalTricksLearned());
    }

    @Test
    void shouldThrowExceptionForNonExistingTrickWhileMarkedAsMastered() {
        // Given
        when(trickRepository.findByName("wrongTrick")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> {
            trickService.setTrickAsMastered("wrongTrick", "user");
        });
    }

    @Test
    void shouldSetTrickAsNotMastered() {
        // Given
        Trick trick2 = new Trick(
                category,
                "testTrick2",
                "testLink2",
                "L",
                "lorem ipsum2"
        );
        user.getUserProgress().getMasteredTricks().add(trick2);
        when(trickRepository.findByName("testTrick2")).thenReturn(Optional.of(trick2));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        trickService.setTrickAsNotMastered("testTrick2", "user");
        UserProgress userProgress1 = user.getUserProgress();

        // Then
        assertFalse(userProgress1.getMasteredTricks().contains(trick2));
    }

    @Test
    void shouldThrowExceptionForNonExistingTrickWhileMarkedAsNotMastered() {
        // Given
        when(trickRepository.findByName("wrongTrick")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(NotFoundException.class, () -> {
            trickService.setTrickAsNotMastered("wrongTrick", "user");
        });
    }

    @Test
    void shouldGetListOfCategoryDtos() {
        // Given
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // When
        List<CategoryDto> result = trickService.getCategories();

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void shouldAddCategory() {
        // Given
        when(categoryRepository.existsByName("cat2")).thenReturn(false);

        // When
        boolean result = trickService.addCategory("cat2");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        // Given
        when(categoryRepository.existsByName("testCategory")).thenReturn(true);

        // When
        // Then
        assertThrows(CategoryAlreadyExistsException.class, () -> {
            trickService.addCategory("testCategory");
        });
    }

    @Test
    void shouldReturnAllTrickDtoList() {
        // Given
        Trick trick2 = new Trick(
                category,
                "testTrick2",
                "testLink2",
                "L",
                "lorem ipsum2"
        );

        when(trickRepository.findAll()).thenReturn(List.of(trick, trick2));

        // When
        List<TrickDto> result = trickService.getAllTricks();

        // Then
        assertEquals(2, result.size());
        assertEquals(trick.getName(), result.getFirst().getTrickName());
        assertEquals(trick2.getName(), result.getLast().getTrickName());
    }

    @Test
    void shouldResetUserProgress() {
        // Given
        Trick trick2 = new Trick(
                category,
                "testTrick2",
                "testLink2",
                "L",
                "lorem ipsum2"
        );
        when(trickRepository.findByUserProgresses_Id(userProgress.getId())).thenReturn(List.of(trick, trick2));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // When
        trickService.resetProgress(user.getUsername());

        UserProgress userProgress1 = user.getUserProgress();
        int ttlAfter = userProgress1.getTotalTricksLearned();
        Set<Trick> mtAfter = userProgress1.getMasteredTricks();

        // Then
        assertEquals(0, ttlAfter);
        assertEquals(0, mtAfter.size());
    }

    @Test
    void shouldReturnAllCategoryDtoList() {
        // Given
        Category category1 = new Category("testCategory1");

        when(categoryRepository.findAll()).thenReturn(List.of(category, category1));

        // When
        List<CategoryDto> result = trickService.getCategories();

        // Then
        assertEquals(2, result.size());
        assertEquals(category.getName(), result.getFirst().getName());
        assertEquals(category1.getName(), result.getLast().getName());
    }

    @Test
    void shouldReturnFalseForDeletingNonExistingCategory() {
        // Given
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.empty());

        // When
        boolean result = trickService.deleteCategory(category.getName());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldDeleteCategorySuccessfully() {
        // Given
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        // When
        boolean result = trickService.deleteCategory(category.getName());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionForAddingExistingTrick() {
        // Given
        TrickDto trickDto = new TrickDto(trick);
        when(trickRepository.existsByName(trick.getName())).thenReturn(true);

        // When
        // Then
        assertThrows(TrickAlreadyExistsException.class, () -> {
            trickService.addTrick(trickDto);
        });
    }

    @Test
    void shouldThrowExceptionForAddingTrickToNonExistingCategory() {
        // Given
        TrickDto trickDto = new TrickDto(trick);
        when(trickRepository.existsByName(trick.getName())).thenReturn(false);
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(NotFoundException.class, () -> {
            trickService.addTrick(trickDto);
        });
    }

    @Test
    void shouldAddTrickSuccessfully() {
        // Given
        TrickDto trickDto = new TrickDto(trick);
        when(trickRepository.existsByName(trick.getName())).thenReturn(false);
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        // When
        boolean result = trickService.addTrick(trickDto);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForDeletingNonExistingTrick() {
        // Given
        when(trickRepository.findByName(trick.getName())).thenReturn(Optional.empty());

        // When
        boolean result = trickService.deleteTrick(trick.getName());

        // Then
        assertFalse(result);
    }

    @Test
    void shouldDeleteTrickSuccessfully() {
        // Given
        when(trickRepository.findByName(trick.getName())).thenReturn(Optional.of(trick));

        // When
        boolean result = trickService.deleteTrick(trick.getName());

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnProgressForUserWithMasteredTricks() {
        // Given
        Category category2 = new Category("category2");
        Trick trick2 = new Trick(category2, "trick2", "link2", "R", "desc2");

        category.setTricks(List.of(trick));
        category2.setTricks(List.of(trick2));

        userProgress.getMasteredTricks().add(trick);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(categoryRepository.findAll()).thenReturn(List.of(category, category2));

        // When
        List<CategoryProgressDto> progress = trickService.getProgress("user");

        // Then
        assertEquals(2, progress.size());

        CategoryProgressDto dto1 = progress.stream()
                .filter(d -> d.getCategoryName().equals("testCategory"))
                .findFirst().orElseThrow();
        CategoryProgressDto dto2 = progress.stream()
                .filter(d -> d.getCategoryName().equals("category2"))
                .findFirst().orElseThrow();

        assertEquals(1, dto1.getTotalTricks());
        assertEquals(1, dto1.getMasteredTricks());

        assertEquals(1, dto2.getTotalTricks());
        assertEquals(0, dto2.getMasteredTricks());
    }

    @Test
    void shouldReturnZeroMasteredWhenUserProgressIsNull() {
        // Given
        user.setUserProgress(null);
        category.setTricks(List.of(trick));

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // When
        List<CategoryProgressDto> progress = trickService.getProgress("user");

        // Then
        assertEquals(1, progress.size());
        CategoryProgressDto dto = progress.get(0);
        assertEquals(1, dto.getTotalTricks());
        assertEquals(0, dto.getMasteredTricks());
    }

    @Test
    void shouldThrowNotFoundExceptionForNonExistingUser() {
        // Given
        when(userRepository.findByUsername("nonUser")).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(NotFoundException.class, () -> {
            trickService.getProgress("nonUser");
        });
    }

    @Test
    void shouldHandleCategoryWithNoTricks() {
        // Given
        Category emptyCategory = new Category("emptyCategory");
        emptyCategory.setTricks(List.of());

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(categoryRepository.findAll()).thenReturn(List.of(emptyCategory));

        // When
        List<CategoryProgressDto> progress = trickService.getProgress("user");

        // Then
        assertEquals(1, progress.size());
        CategoryProgressDto dto = progress.get(0);
        assertEquals(0, dto.getTotalTricks());
        assertEquals(0, dto.getMasteredTricks());
    }

}
