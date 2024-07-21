package ar.com.dno.ai.rag.controlplane.spaces.domain.exceptions;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class NamespaceAlreadyAssignedException extends RuntimeException {
    private final Space.Id spaceId;


    @Override
    public String getMessage() {
        return "[space.name:%s][space.provider:%s][space.model:%s] Namespace already assigned".formatted(
                spaceId.name(),
                spaceId.model().provider(),
                spaceId.model().provider()
        );
    }
}
