package ar.com.dno.ai.rag.controlplane.models.adapters.input.web;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.usecases.DeprecateSupportedModelUseCase;
import ar.com.dno.ai.rag.controlplane.models.usecases.ListSupportedModelsUseCase;
import ar.com.dno.ai.rag.controlplane.models.usecases.OverrideSupportedModelMetadataUseCase;
import ar.com.dno.ai.rag.controlplane.models.usecases.RegisterSupportedModelUseCase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@RestController
public class SupportedModelWebController {
    private final ListSupportedModelsUseCase listSupportedModels;
    private final RegisterSupportedModelUseCase registerSupportedModel;
    private final OverrideSupportedModelMetadataUseCase overrideSupportedModelMetadata;
    private final DeprecateSupportedModelUseCase disableSupportedModel;


    @GetMapping(path = "/admin/supported_models")
    ResponseEntity<List<SupportedModel>> listSupportedModels() {
        final List<SupportedModel> supportedModels = listSupportedModels.handle(new ListSupportedModelsUseCase.Query());
        return ResponseEntity.ok(supportedModels);
    }

    @PostMapping(path = "/admin/providers/{provider}/models/{model}")
    ResponseEntity<Void> registerSupportedModel(@PathVariable("provider") String provider,
                                                @PathVariable("model") String name,
                                                @RequestBody RegisterSupportedModelRequest webRequest) {
        final RegisterSupportedModelUseCase.Request request = new RegisterSupportedModelUseCase.Request(
                new SupportedModel.Provider(provider),
                new SupportedModel.Name(name),
                webRequest.supportedFormats(),
                webRequest.metadata()
        );
        registerSupportedModel.handle(request);

        return ResponseEntity.created(URI.create("")).build();
    }

    @PutMapping(path = "/admin/providers/{provider}/models/{model}/metadata")
    ResponseEntity<Void> overrideMetadata(@PathVariable("provider") String provider,
                                          @PathVariable("model") String name,
                                          @RequestBody JsonNode metadata) {

        final OverrideSupportedModelMetadataUseCase.Request request = new OverrideSupportedModelMetadataUseCase.Request(
                new SupportedModel.Provider(provider),
                new SupportedModel.Name(name),
                metadata
        );

        overrideSupportedModelMetadata.handle(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/admin/providers/{provider}/models/{model}")
    ResponseEntity<Void> deprecateSupportedModel(@PathVariable("provider") String provider,
                                               @PathVariable("model") String name) {

        final DeprecateSupportedModelUseCase.Request request = new DeprecateSupportedModelUseCase.Request(
                new SupportedModel.Provider(provider),
                new SupportedModel.Name(name)
        );

        disableSupportedModel.handle(request);

        return ResponseEntity.noContent().build();
    }


    public record RegisterSupportedModelRequest(
            @JsonProperty("supported_formats") Set<SupportedModel.InputFormat> supportedFormats,
            @JsonProperty("metadata") JsonNode metadata) {
    }
}
