package ar.com.dno.ai.rag.controlplane.spaces.adapters.input.listeners;


import ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events.SpaceDisableTriggered;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.DisableSpaceUseCase;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "cdc", havingValue = "memory")
public class DisableSpaceEventListener {
    private final DisableSpaceUseCase disableSpace;


    @EventListener
    public void handle(SpaceDisableTriggered event) {
        disableSpace.handle(new DisableSpaceUseCase.Request(event.spaceId()));
    }
}
