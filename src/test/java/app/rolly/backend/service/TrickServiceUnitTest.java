package app.rolly.backend.service;

import app.rolly.backend.dto.TrickDto;
import app.rolly.backend.model.*;
import app.rolly.backend.repository.CategoryRepository;
import app.rolly.backend.repository.TrickRepository;
import app.rolly.backend.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @InjectMocks
    private TrickService trickService;

    private Category category;
    private Trick trick;
    private UserProgress userProgress;

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

        User user = new User(
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

        trick.getUserProgresses().add(userProgress);
        userProgress.getMasteredTricks().add(trick);
    }

    @Test
    void shouldReturnTrickDto(){
        // Given
        when(trickRepository.findByName("testTrick")).thenReturn(trick);

        // When
        TrickDto trickDto = trickService.getTrick("testTrick");

        // Then
        assertNotNull(trickDto);
        assertEquals(trick.getCategory().getName(), trickDto.getCategoryName());
        assertEquals(trick.getName(), trickDto.getTrickName());
        assertEquals(trick.getLink(), trickDto.getLink());
        assertEquals(trick.getLeg(), trickDto.getLeg());
        assertEquals(trick.getDescription(), trickDto.getDescription());
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
        when(categoryRepository.findByName("testCategory")).thenReturn(category);

        // When
        List<TrickDto> tricks = trickService.getTricksByCategory("testCategory", userProgress);

        // Then
        assertEquals(2, tricks.size());
        assertEquals(trick.getName(), tricks.getFirst().getTrickName());
        assertEquals(trick2.getName(), tricks.getLast().getTrickName());
        assertEquals("testCategory", tricks.getFirst().getCategoryName());
        assertEquals("testCategory", tricks.getLast().getCategoryName());
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
        when(trickRepository.findByName("testTrick2")).thenReturn(trick2);

        // When
        trickService.setTrickAsMastered("testTrick2", userProgress);

        // Then
        assertTrue(userProgress.getMasteredTricks().contains(trick2));
    }


}
