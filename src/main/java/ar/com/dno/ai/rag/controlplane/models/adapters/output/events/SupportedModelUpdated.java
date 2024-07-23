package ar.com.dno.ai.rag.controlplane.models.adapters.output.events;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;

import java.time.Instant;


public record SupportedModelUpdated(SupportedModel.Id modelId, Instant instant) {
    public SupportedModelUpdated(SupportedModel.Id modelId) {
        this(modelId, Instant.now());
    }
}
