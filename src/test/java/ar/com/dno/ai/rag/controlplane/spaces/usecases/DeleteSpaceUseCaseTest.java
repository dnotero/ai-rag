package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.commons.domain.Criticality;
import ar.com.dno.ai.rag.controlplane.spaces.adapters.output.memory.MemorySpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DeleteSpaceUseCaseTest {
    @Autowired
    private MemorySpaceRepository spaceRepository;
    @Autowired
    private DeleteSpaceUseCase useCase;


    @Test
    void noSpaceFound() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model-%s".formatted(Instant.now()));
        final DeleteSpaceUseCase.Request request = new DeleteSpaceUseCase.Request(name, model);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertThrows(SpaceNotFoundException.class, testCase);
    }

    @Test
    void spaceDisabled() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model-%s".formatted(Instant.now()));
        final Space.Id id = new Space.Id(name, model);
        final Space originalSpace = new Space(name, model, Criticality.TEST);
        final DeleteSpaceUseCase.Request request = new DeleteSpaceUseCase.Request(name, model);

        spaceRepository.save(originalSpace);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertDoesNotThrow(testCase);

        final Space space = spaceRepository.findBy(id).orElseThrow();
        assertEquals(name, space.name());
        assertEquals(model, space.model());
        assertEquals(Space.Status.DELETED, space.status());
    }
}
