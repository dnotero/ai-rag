package ar.com.dno.ai.embeddings.spaces.usecases;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ListSpaceVersionsUseCaseTest {
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private ListSpaceVersionsUseCase useCase;


    @Test
    void noSpacesFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final ListSpaceVersionsUseCase.Query query = new ListSpaceVersionsUseCase.Query(name);

        // When
        final List<Space> spaces = useCase.query(query);

        // Then
        assertEquals(List.of(), spaces);
    }

    @Test
    void spacesAreFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model");
        final Space space = new Space(name, model);
        final ListSpaceVersionsUseCase.Query query = new ListSpaceVersionsUseCase.Query(name);

        spaceRepository.save(space);

        // When
        final List<Space> spaces = useCase.query(query);

        // Then
        assertEquals(List.of(space), spaces);
    }
}
