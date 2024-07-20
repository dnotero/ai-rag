package ar.com.dno.ai.rag.controlplane.models.usecases;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
import ar.com.dno.ai.rag.controlplane.models.usecases.exceptions.SupportedModelNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class OverrideSupportedModelMetadataUseCase {
    private final SupportedModelRepository modelRepository;


    public void handle(OverrideSupportedModelMetadataUseCase.Request request) {
        final SupportedModel.Id id = request.modelId();
        final SupportedModel supportedModel = modelRepository.findBy(id)
                .orElseThrow(() -> new SupportedModelNotFoundException(id));

        final SupportedModel updatedSupportedModel = supportedModel.metadata(request.metadata());

        modelRepository.save(updatedSupportedModel);
    }


    public record Request(SupportedModel.Provider provider, SupportedModel.Name name, JsonNode metadata) {
        public SupportedModel.Id modelId() {
            return new SupportedModel.Id(this.provider, this.name);
        }
    }
}
