package ar.com.dno.ai.rag.controlplane.models.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class SupportedModelNotFoundException extends RuntimeException {
    SupportedModel.Id modelId;
}
