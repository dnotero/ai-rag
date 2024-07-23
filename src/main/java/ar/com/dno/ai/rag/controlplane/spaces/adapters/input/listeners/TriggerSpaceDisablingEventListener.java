package ar.com.dno.ai.rag.controlplane.spaces.adapters.input.listeners;


import ar.com.dno.ai.rag.controlplane.models.adapters.output.events.SupportedModelUpdated;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
import ar.com.dno.ai.rag.controlplane.models.usecases.exceptions.SupportedModelNotFoundException;
import ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events.SpaceDisableTriggered;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "cdc", havingValue = "memory")
public class TriggerSpaceDisablingEventListener {
    private final SupportedModelRepository supportedModelRepository;
    private final SpaceSearchService spaceSearchService;
    private final ApplicationEventPublisher eventPublisher;


    @EventListener
    public void handle(SupportedModelUpdated event) {
        final SupportedModel supportedModel = supportedModelRepository.findBy(event.modelId())
                .orElseThrow(() -> new SupportedModelNotFoundException(event.modelId()));

        if (supportedModel.isDeprecated()) {
            final SupportedModel.Id modelId = event.modelId();
            final Space.Model model = new Space.Model(modelId.provider().value(), modelId.name().value());
            spaceSearchService.findAllBy(model, Space.Status.ACTIVE).stream()
                    .map(Space::id)
                    .map(SpaceDisableTriggered::new)
                    .forEach(eventPublisher::publishEvent);

        }

    }
}
