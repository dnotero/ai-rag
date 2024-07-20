package ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode(callSuper = false)
public class SpaceAlreadyExistsException extends RuntimeException {
    Space.Id spaceId;
}
