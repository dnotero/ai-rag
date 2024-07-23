package ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events.publishers;


import ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events.SpaceCreated;
import ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events.SpaceUpdated;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@AllArgsConstructor
@ConditionalOnProperty(name = "cdc", havingValue = "memory")
@Primary
@Repository
public class ApplicationEventPublisherSpaceRepository implements SpaceRepository {
    private final SpaceRepository delegate;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Optional<Space> findBy(Space.Id id) {
        return delegate.findBy(id);
    }

    @Override
    public void save(Space space) {
        final boolean isNewSpace = isNew(space);
        delegate.save(space);

        if (isNewSpace) {
            eventPublisher.publishEvent(new SpaceCreated(space.id()));
        } else {
            eventPublisher.publishEvent(new SpaceUpdated(space.id()));
        }
    }

    private boolean isNew(Space space) {
        return this.findBy(space.id()).isEmpty();
    }
}
