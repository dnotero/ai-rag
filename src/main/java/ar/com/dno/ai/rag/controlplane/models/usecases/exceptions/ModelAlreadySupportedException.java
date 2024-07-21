package ar.com.dno.ai.rag.controlplane.models.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class ModelAlreadySupportedException extends RuntimeException {
    private final SupportedModel.Id id;
}
