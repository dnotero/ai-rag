package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.commons.domain.Criticality;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ListSpacesUseCaseTest {
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private ListSpacesUseCase useCase;


    @Test
    void noSpacesFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final ListSpacesUseCase.Query query = new ListSpacesUseCase.Query(name);

        // When
        final List<Space> spaces = useCase.handle(query);

        // Then
        assertEquals(List.of(), spaces);
    }

    @Test
    void spacesAreFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model");
        final Space space = new Space(name, model, Criticality.TEST);
        final ListSpacesUseCase.Query query = new ListSpacesUseCase.Query(name);

        spaceRepository.save(space);

        // When
        final List<Space> spaces = useCase.handle(query);

        // Then
        assertEquals(List.of(space), spaces);
    }
}
