package app.rolly.backend.service;

import app.rolly.backend.dto.CategoryDto;
import app.rolly.backend.dto.TrickDto;
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
    void set(){
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
    void shouldReturnTrickDto(){
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
    void shouldThrowExceptionForNonExistingTrick(){
        // Given
        when(trickRepository.findByName("wrongTrick")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, ()->{
            trickService.getTrick("wrongTrick", "user");
        });
    }

    @Test
    void shouldReturnListOfTrickDtoForCategory(){
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
    void shouldReturnEmptyTrickListForCategory(){
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
    void shouldThrowExceptionForNonExistingCategory(){
        // Given
        when(categoryRepository.findByName("wrongCategory")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, ()->{
            trickService.getTricksByCategory("wrongCategory", "user");
        });
    }

    @Test
    void shouldSetTrickAsMastered(){
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
    void shouldThrowExceptionForNonExistingTrickWhileMarkedAsMastered(){
        // Given
        when(trickRepository.findByName("wrongTrick")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, ()->{
            trickService.setTrickAsMastered("wrongTrick", "user");
        });
    }

    @Test
    void shouldGetListOfCategoryDtos(){
        // Given
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // When
        List<CategoryDto> result = trickService.getCategories();

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void shouldAddCategory(){
        // Given
        when(categoryRepository.existsByName("cat2")).thenReturn(false);

        // When
        boolean result = trickService.addCategory("cat2");

        // Then
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists(){
        // Given
        when(categoryRepository.existsByName("testCategory")).thenReturn(true);

        // When
        // Then
        assertThrows(RuntimeException.class, ()->{
            trickService.addCategory("testCategory");
        });
    }
}
