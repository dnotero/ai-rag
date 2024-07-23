package ar.com.dno.ai.rag.controlplane.models.adapters.output.events.publishers;


import ar.com.dno.ai.rag.controlplane.models.adapters.output.events.SupportedModelCreated;
import ar.com.dno.ai.rag.controlplane.models.adapters.output.events.SupportedModelUpdated;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
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
public class EventPublisherSupportedModelRepository implements SupportedModelRepository {
    private final SupportedModelRepository delegate;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Optional<SupportedModel> findBy(SupportedModel.Id id) {
        return delegate.findBy(id);
    }

    @Override
    public void save(SupportedModel supportedModel) {
        final boolean isNew = isNew(supportedModel);
        delegate.save(supportedModel);

        if (isNew) {
            eventPublisher.publishEvent(new SupportedModelCreated(supportedModel.id()));
        } else {
            eventPublisher.publishEvent(new SupportedModelUpdated(supportedModel.id()));
        }
    }

    private boolean isNew(SupportedModel supportedModel) {
        return this.findBy(supportedModel.id()).isEmpty();
    }
}
