package ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class NamespaceNotFoundException extends RuntimeException {
    private final Namespace.Name namespace;
}
