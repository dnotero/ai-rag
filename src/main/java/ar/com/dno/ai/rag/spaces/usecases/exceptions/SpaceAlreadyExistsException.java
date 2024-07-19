package ar.com.dno.ai.rag.spaces.usecases.exceptions;


import ar.com.dno.ai.rag.spaces.domain.Space;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode(callSuper = false)
public class SpaceAlreadyExistsException extends RuntimeException {
    Space.Name name;
    Space.Model model;
}
