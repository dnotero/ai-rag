package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.commons.domain.Criticality;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GetSpaceUseCaseTest {
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private GetSpaceUseCase useCase;


    @Test
    void noSpacesFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model");
        final GetSpaceUseCase.Query query = new GetSpaceUseCase.Query(name, model);

        // When
        final Optional<Space> optionalSpace = useCase.handle(query);

        // Then
        assertEquals(Optional.empty(), optionalSpace);
    }

    @Test
    void spacesAreFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model");
        final Space space = new Space(name, model, Criticality.TEST);
        final GetSpaceUseCase.Query query = new GetSpaceUseCase.Query(name, model);

        spaceRepository.save(space);

        // When
        final Optional<Space> optionalSpace = useCase.handle(query);

        // Then
        assertEquals(Optional.of(space), optionalSpace);
    }
}
