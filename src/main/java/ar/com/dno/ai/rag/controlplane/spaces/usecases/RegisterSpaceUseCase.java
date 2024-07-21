package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.commons.Criticality;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelSearchService;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.ModelNotSupportedException;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class RegisterSpaceUseCase {
    private final SpaceRepository spaceRepository;
    private final SupportedModelSearchService modelSearchService;


    public void handle(Request request) {
        final Space.Id spaceId = request.spaceId();
        final Optional<Space> optionalSpace = spaceRepository.findById(spaceId);

        if(optionalSpace.isPresent()) {
            throw new SpaceAlreadyExistsException(spaceId);
        }

        final SupportedModel.Id supportedModelId = request.model().toSupportedModelId();
        final SupportedModel supportedModel = modelSearchService.findBy(supportedModelId)
                .orElseThrow(() -> new ModelNotSupportedException(supportedModelId));

        if (!supportedModel.isEnabled()) {
            throw new ModelNotSupportedException(supportedModelId);
        }

        final Space space = new Space(request.name(), request.model(), request.criticality());
        spaceRepository.save(space);
    }


    public record Request(Space.Name name, Space.Model model, Criticality criticality) {
        public Space.Id spaceId() {
            return new Space.Id(this.name, this.model);
        }
    }
}
