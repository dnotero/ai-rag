package ar.com.dno.ai.embeddings.spaces.adapters.input.web;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.usecases.RegisterSpaceUseCase;
import ar.com.dno.ai.embeddings.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@AllArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpacesWebController {
    private RegisterSpaceUseCase registerSpace;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> registerSpace(@RequestBody SpacesWebController.CreateSpaceWebRequest webRequest) {
        registerSpace.handle(webRequest.request());
        return ResponseEntity.created(URI.create("")).build();
    }


    public record CreateSpaceWebRequest(@JsonProperty("name") String name, @JsonProperty("model") Space.Model model) {
        RegisterSpaceUseCase.Request request() {
            return new RegisterSpaceUseCase.Request(new Space.Name(this.name), this.model);
        }
    }
}
