package ar.com.dno.ai.rag.controlplane.spaces.adapters.input.listeners;


import ar.com.dno.ai.rag.controlplane.spaces.adapters.output.events.SpaceCreated;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.AssignNamespaceUseCase;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "cdc", havingValue = "memory")
public class AssignNamespaceEventListener {
    private final AssignNamespaceUseCase assignNamespace;


    @EventListener
    public void handle(SpaceCreated event) {
        assignNamespace.handle(new AssignNamespaceUseCase.Request(event.spaceId()));
    }
}
