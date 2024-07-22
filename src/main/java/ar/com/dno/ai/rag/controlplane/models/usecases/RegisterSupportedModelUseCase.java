package ar.com.dno.ai.rag.controlplane.models.usecases;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
import ar.com.dno.ai.rag.controlplane.models.usecases.exceptions.ModelAlreadySupportedException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class RegisterSupportedModelUseCase {
    private final SupportedModelRepository modelRepository;


    @Transactional
    public void handle(RegisterSupportedModelUseCase.Request request) {
        final SupportedModel.Id id = request.modelId();
        final Optional<SupportedModel> optionalSupportedModel = modelRepository.findBy(id);

        if (optionalSupportedModel.isPresent()) {
            throw new ModelAlreadySupportedException(id);
        }

        final SupportedModel supportedModel = new SupportedModel(request.provider(), request.name(), request.metadata());
        modelRepository.save(supportedModel);
    }


    public record Request(SupportedModel.Provider provider, SupportedModel.Name name, JsonNode metadata) {
        public SupportedModel.Id modelId() {
            return new SupportedModel.Id(this.provider, this.name);
        }
    }
}
