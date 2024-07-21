package ar.com.dno.ai.rag.controlplane.namespaces.adapters.input.web;


import ar.com.dno.ai.rag.controlplane.commons.Criticality;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.usecases.RegisterNamespaceUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@AllArgsConstructor
@RestController
@RequestMapping("/admin/namespaces")
public class NamespaceWebController {
    private final RegisterNamespaceUseCase registerNamespace;


    @PostMapping
    ResponseEntity<Void> registerSpace(@RequestBody NamespaceWebController.RegisterNamespaceWebRequest webRequest) {
        registerNamespace.handle(webRequest.request());
        return ResponseEntity.created(URI.create("")).build();
    }


    public record RegisterNamespaceWebRequest(String name, String criticality) {
        public RegisterNamespaceUseCase.Request request() {
            return new RegisterNamespaceUseCase.Request(
                    new Namespace.Name(this.name),
                    Criticality.of(this.criticality)
            );
        }
    }
}
