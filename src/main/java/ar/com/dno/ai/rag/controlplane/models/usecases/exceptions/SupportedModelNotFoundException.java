package ar.com.dno.ai.rag.controlplane.models.usecases.exceptions;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode(callSuper = false)
public class SupportedModelNotFoundException extends RuntimeException {
    SupportedModel.Id spaceId;
}
