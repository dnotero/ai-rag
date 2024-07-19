package ar.com.dno.ai.rag.spaces.usecases.exceptions;


import ar.com.dno.ai.rag.spaces.domain.Space;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class SpaceNotFoundException extends RuntimeException {
    private final Space.Id spaceId;
}
