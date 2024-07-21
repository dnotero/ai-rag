package ar.com.dno.ai.rag.controlplane.namespaces.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class NamespaceAlreadyExistException extends RuntimeException {
    private final Namespace.Name name;


    @Override
    public String getMessage() {
        return "[namespace.name:%s] Namespace already exists".formatted(this.name);
    }
}
