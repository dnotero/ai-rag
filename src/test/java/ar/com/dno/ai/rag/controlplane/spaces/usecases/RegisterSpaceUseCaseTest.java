package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.commons.domain.Criticality;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.usecases.RegisterSupportedModelUseCase;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.usecases.RegisterNamespaceUseCase;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.ModelNotSupportedException;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RegisterSpaceUseCaseTest {
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private RegisterSpaceUseCase useCase;
    @Autowired
    private RegisterSupportedModelUseCase registerSupportedModel;
    @Autowired
    private RegisterNamespaceUseCase registerNamespace;


    @Test
    void unsupportedModelsNotAllowed() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model-%s".formatted(Instant.now()));
        final Space.Id id = new Space.Id(name, model);
        final RegisterSpaceUseCase.Request request = new RegisterSpaceUseCase.Request(name, model, Criticality.TEST);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertThrows(ModelNotSupportedException.class, testCase);
        assertEquals(Optional.empty(), spaceRepository.findById(id));
    }

    @Test
    void noDuplicateSpacesAllowed() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model-%s".formatted(Instant.now()));
        final Space.Id id = new Space.Id(name, model);
        final Space space = new Space(name, model, Criticality.TEST);
        final RegisterSpaceUseCase.Request request = new RegisterSpaceUseCase.Request(name, model, Criticality.TEST);

        registerSupportedModel.handle(new RegisterSupportedModelUseCase.Request(
                new SupportedModel.Provider(model.provider()),
                new SupportedModel.Name(model.name()),
                Set.of(SupportedModel.InputFormat.TEXT),
                null
        ));

        spaceRepository.save(space);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertThrows(SpaceAlreadyExistsException.class, testCase);
        assertEquals(space, spaceRepository.findById(id).orElseThrow());
    }

    @Test
    void spaceIsRegisteredCorrectly() {
        // Given
        final Space.Name name = new Space.Name("test-%s".formatted(Instant.now()));
        final Space.Model model = new Space.Model("provider", "model-%s".formatted(Instant.now()));
        final Space.Id id = new Space.Id(name, model);
        final RegisterSpaceUseCase.Request request = new RegisterSpaceUseCase.Request(name, model, Criticality.TEST);

        registerNamespace.handle(new RegisterNamespaceUseCase.Request(
                new Namespace.Name("prod-test"),
                Criticality.TEST
        ));

        registerSupportedModel.handle(new RegisterSupportedModelUseCase.Request(
                new SupportedModel.Provider(model.provider()),
                new SupportedModel.Name(model.name()),
                Set.of(SupportedModel.InputFormat.TEXT),
                null
        ));

        final Optional<Space> beforeSave = spaceRepository.findById(id);

        // When
        final Executable testCase = () -> useCase.handle(request);

        // Then
        assertDoesNotThrow(testCase);

        final Space space = spaceRepository.findById(id).orElseThrow();
        assertEquals(Optional.empty(), beforeSave);
        assertEquals(name, space.name());
        assertEquals(model, space.model());
        assertEquals(Space.Status.CREATED, space.status());
        assertNotNull(space.namespace());
    }
}
