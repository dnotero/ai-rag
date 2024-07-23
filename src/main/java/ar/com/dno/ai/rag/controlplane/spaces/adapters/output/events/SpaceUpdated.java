package ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;

import java.time.Instant;


public record SpaceUpdated(Space.Id spaceId, Instant instant) {
    public SpaceUpdated(Space.Id spaceId) {
        this(spaceId, Instant.now());
    }
}
