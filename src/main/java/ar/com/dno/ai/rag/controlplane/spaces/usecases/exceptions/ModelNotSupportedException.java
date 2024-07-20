package ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode(callSuper = false)
public class ModelNotSupportedException extends RuntimeException {
    SupportedModel.Id modelId;
}
