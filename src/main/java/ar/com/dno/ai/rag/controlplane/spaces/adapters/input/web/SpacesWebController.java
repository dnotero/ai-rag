package ar.com.dno.ai.rag.controlplane.spaces.adapters.input.web;


import ar.com.dno.ai.rag.commons.domain.Criticality;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.DisableSpaceUseCase;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.GetSpaceUseCase;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.ListSpacesUseCase;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.RegisterSpaceUseCase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
@RequestMapping("/admin/spaces")
public class SpacesWebController {
    private RegisterSpaceUseCase registerSpace;
    private GetSpaceUseCase getSpace;
    private ListSpacesUseCase listSpaces;
    private DisableSpaceUseCase disableSpace;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> registerSpace(@RequestBody SpacesWebController.CreateSpaceWebRequest webRequest) {
        registerSpace.handle(webRequest.request());

        return ResponseEntity.created(URI.create("")).build();
    }

    @GetMapping(path = "/{name}")
    ResponseEntity<List<Space>> listSpacesByName(@PathVariable("name") String name) {
        final ListSpacesUseCase.Query query = new ListSpacesUseCase.Query(new Space.Name(name));
        final List<Space> spaces = listSpaces.handle(query);

        return ResponseEntity.ok(spaces);
    }

    @GetMapping(path = "/{name}/providers/{provider}/models/{model}")
    ResponseEntity<Space> getSpace(@PathVariable("name") String name,
                                   @PathVariable("provider") String provider,
                                   @PathVariable("model") String model) {
        final Space.Name spaceName = new Space.Name(name);
        final Space.Model spaceModel = new Space.Model(provider, model);
        final GetSpaceUseCase.Query query = new GetSpaceUseCase.Query(new Space.Id(spaceName, spaceModel));
        final Optional<Space> optionalSpace = getSpace.handle(query);

        return ResponseEntity.of(optionalSpace);
    }

    @DeleteMapping(path = "/{name}/providers/{provider}/models/{model}")
    ResponseEntity<Void> disableSpace(@PathVariable("name") String name,
                                      @PathVariable("provider") String provider,
                                      @PathVariable("model") String model) {
        final Space.Name spaceName = new Space.Name(name);
        final Space.Model spaceModel = new Space.Model(provider, model);
        final DisableSpaceUseCase.Request request = new DisableSpaceUseCase.Request(new Space.Id(spaceName, spaceModel));

        disableSpace.handle(request);

        return ResponseEntity.noContent().build();
    }

    public record CreateSpaceWebRequest(@JsonProperty("name") String name,
                                        @JsonProperty("model") Space.Model model,
                                        @JsonProperty("criticality") Criticality criticality) {
        RegisterSpaceUseCase.Request request() {
            return new RegisterSpaceUseCase.Request(new Space.Name(this.name), this.model, this.criticality);
        }
    }
}
