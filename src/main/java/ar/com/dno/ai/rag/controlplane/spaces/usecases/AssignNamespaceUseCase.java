package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceSearchService;
import ar.com.dno.ai.rag.controlplane.spaces.domain.NamespaceSelector;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;


@AllArgsConstructor
@Service
public class AssignNamespaceUseCase {
    private final SpaceRepository spaceRepository;
    private final NamespaceSearchService namespaceSearchService;
    private final NamespaceSelector namespaceSelector;


    @Transactional
    public void handle(AssignNamespaceUseCase.Request request) {
        final Space space = spaceRepository.findBy(request.spaceId())
                .orElseThrow(() -> new SpaceNotFoundException(request.spaceId()));

        if (nonNull(space.namespace())) {
            return;
        }
        final List<Namespace> namespaces = namespaceSearchService.findAll();
        final Namespace namespace = namespaceSelector.select(space, namespaces).orElseThrow(RuntimeException::new);
        final Space assignedSpace = space.assign(namespace);
        spaceRepository.save(assignedSpace);
    }

    public record Request(Space.Id spaceId) {}
}
