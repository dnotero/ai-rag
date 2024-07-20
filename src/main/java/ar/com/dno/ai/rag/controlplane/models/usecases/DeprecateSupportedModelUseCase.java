package ar.com.dno.ai.rag.controlplane.models.usecases;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
import ar.com.dno.ai.rag.controlplane.models.usecases.exceptions.SupportedModelNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class DeprecateSupportedModelUseCase {
    private final SupportedModelRepository modelRepository;


    public void handle(DeprecateSupportedModelUseCase.Request request) {
        final SupportedModel.Id id = request.modelId();
        final SupportedModel supportedModel = modelRepository.findById(id)
                .orElseThrow(() -> new SupportedModelNotFoundException(id));

        final SupportedModel disabledSupportedModel = supportedModel.deprecate();

        modelRepository.save(disabledSupportedModel);
    }


    public record Request(SupportedModel.Provider provider, SupportedModel.Name name) {
        public SupportedModel.Id modelId() {
            return new SupportedModel.Id(this.provider, this.name);
        }
    }
}
