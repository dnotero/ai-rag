package ar.com.dno.ai.rag.controlplane.namespaces.usecases;


import ar.com.dno.ai.rag.controlplane.commons.Criticality;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceRepository;
import ar.com.dno.ai.rag.controlplane.namespaces.usecases.exceptions.NamespaceAlreadyExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class RegisterNamespaceUseCase {
    private final NamespaceRepository namespaceRepository;


    public void handle(RegisterNamespaceUseCase.Request request) {
        final Optional<Namespace> optionalNamespace = namespaceRepository.findBy(request.name());

        if (optionalNamespace.isPresent()) {
            throw new NamespaceAlreadyExistException(request.name());
        }

        final Namespace namespace = new Namespace(request.name(), request.criticality());

        namespaceRepository.save(namespace);
    }


    public record Request(Namespace.Name name, Criticality criticality) {
    }
}
