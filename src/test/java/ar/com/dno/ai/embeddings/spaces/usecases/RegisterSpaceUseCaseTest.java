package ar.com.dno.ai.embeddings.spaces.usecases;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceRepository;
import ar.com.dno.ai.embeddings.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RegisterSpaceUseCaseTest {
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private RegisterSpaceUseCase useCase;


    @Test
    void noDuplicateSpacesAllowed() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model");
        final Space space = new Space(name, model);
        final RegisterSpaceUseCase.Request request = new RegisterSpaceUseCase.Request(name, model);

        spaceRepository.save(space);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertThrows(SpaceAlreadyExistsException.class, testCase);
        assertEquals(space, spaceRepository.findByNameAndModel(name, model).orElseThrow());

    }

    @Test
    void spaceIsRegisteredCorrectly() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model");
        final RegisterSpaceUseCase.Request request = new RegisterSpaceUseCase.Request(name, model);

        final Optional<Space> beforeSave = spaceRepository.findByNameAndModel(name, model);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertDoesNotThrow(testCase);

        final Space space = spaceRepository.findByNameAndModel(name, model).orElseThrow();
        assertEquals(Optional.empty(), beforeSave);
        assertEquals(name, space.name());
        assertEquals(model, space.model());
    }
}
