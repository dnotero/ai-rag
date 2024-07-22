package ar.com.dno.ai.rag.controlplane.namespaces.adapters.input.web;


import ar.com.dno.ai.rag.commons.domain.Criticality;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceSearchService;
import ar.com.dno.ai.rag.controlplane.namespaces.usecases.RegisterNamespaceUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/admin/namespaces")
public class NamespaceWebController {
    private final RegisterNamespaceUseCase registerNamespace;
    private final NamespaceSearchService namespaceSearchService;


    @PostMapping
    ResponseEntity<Void> registerNamespace(@RequestBody NamespaceWebController.RegisterNamespaceWebRequest webRequest) {
        registerNamespace.handle(webRequest.request());
        return ResponseEntity.created(URI.create("")).build();
    }

    @GetMapping
    ResponseEntity<List<Namespace>> listNamespace() {
        return ResponseEntity.ok(namespaceSearchService.findAll());
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
