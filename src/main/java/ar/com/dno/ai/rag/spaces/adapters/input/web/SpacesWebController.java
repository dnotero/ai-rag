package ar.com.dno.ai.rag.spaces.adapters.input.web;


import ar.com.dno.ai.rag.spaces.domain.Space;
import ar.com.dno.ai.rag.spaces.domain.SpaceSearchService;
import ar.com.dno.ai.rag.spaces.usecases.RegisterSpaceUseCase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpacesWebController {
    private RegisterSpaceUseCase registerSpace;
    private SpaceSearchService spaceSearchService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> registerSpace(@RequestBody SpacesWebController.CreateSpaceWebRequest webRequest) {
        registerSpace.handle(webRequest.request());
        return ResponseEntity.created(URI.create("")).build();
    }

    @GetMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Space>> listSpacesByName(@PathVariable("name") String name) {
        final List<Space> spaces = spaceSearchService.findByName(new Space.Name(name));
        return ResponseEntity.ok(spaces);
    }


    public record CreateSpaceWebRequest(@JsonProperty("name") String name, @JsonProperty("model") Space.Model model) {
        RegisterSpaceUseCase.Request request() {
            return new RegisterSpaceUseCase.Request(new Space.Name(this.name), this.model);
        }
    }
}
