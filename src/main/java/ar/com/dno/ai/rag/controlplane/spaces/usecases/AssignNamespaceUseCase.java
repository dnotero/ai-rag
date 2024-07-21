package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceSearchService;
import ar.com.dno.ai.rag.controlplane.spaces.domain.NamespaceSelector;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class AssignNamespaceUseCase {
    private final SpaceRepository spaceRepository;
    private final NamespaceSearchService namespaceSearchService;
    private final NamespaceSelector namespaceSelector;


    public void handle(AssignNamespaceUseCase.Request request) {
        final Space.Id spaceId = request.spaceId();
        final Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new SpaceNotFoundException(spaceId));
        final List<Namespace> namespaces = namespaceSearchService.findAll();
        final Namespace namespace = namespaceSelector.select(space, namespaces).orElseThrow(RuntimeException::new);

        spaceRepository.save(space.assign(namespace));
    }


    public record Request(Space.Id spaceId, Namespace.Name namespace) {
    }
}
