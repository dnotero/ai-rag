package ar.com.dno.ai.rag.controlplane.namespaces.adapters.input.web;


import ar.com.dno.ai.rag.controlplane.commons.Criticality;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NamespacesWebControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private NamespaceRepository namespaceRepository;


    @Test
    void noDuplicateNamespacesAllowed() throws Exception {
        // Given
        final String nameValue = "test-%s".formatted(Instant.now().toEpochMilli());
        final String body =
                """
                    {
                        "name": "%s",
                        "criticality": "TEST"
                    }
                """.formatted(nameValue);

        final Namespace.Name name = new Namespace.Name(nameValue);
        final Namespace namespace = new Namespace(name, Criticality.TEST);
        namespaceRepository.save(namespace);

        // When
        final ResultActions results = mvc.perform(
                post("/admin/namespaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isConflict());
        assertEquals(namespace, namespaceRepository.findBy(name).orElseThrow());
    }

    @Test
    void namespaceIsRegisteredCorrectly() throws Exception {
        // Given
        final String nameValue = "test-%s".formatted(Instant.now().toEpochMilli());
        final Namespace.Name name = new Namespace.Name(nameValue);
        final Namespace namespace = new Namespace(name, Criticality.TEST);
        final String body =
                """
                    {
                        "name": "%s",
                        "criticality": "TEST"
                    }
                """.formatted(nameValue);

        // When
        final ResultActions results = mvc.perform(
                post("/admin/namespaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isCreated());
        assertEquals(namespace, namespaceRepository.findBy(name).orElseThrow());
    }

}
