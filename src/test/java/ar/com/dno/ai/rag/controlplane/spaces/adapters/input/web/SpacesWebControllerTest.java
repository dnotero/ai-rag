package ar.com.dno.ai.rag.controlplane.spaces.adapters.input.web;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpacesWebControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SpaceRepository spaceRepository;


    @Test
    void noDuplicateSpacesAllowed() throws Exception {
        // Given
        final String spaceName = "test-%s".formatted(Instant.now().toEpochMilli());
        final Space.Name name = new Space.Name(spaceName);
        final Space.Model model = new Space.Model("openai", "text-embedding-3-small");
        final Space.Id id = new Space.Id(name, model);
        final Space space = new Space(name, model);
        final String body =
                """
                    {
                        "name": "%s",
                        "model": {
                            "provider": "openai",
                            "name": "text-embedding-3-small"
                        }
                    }
                """.formatted(spaceName);

        spaceRepository.save(space);

        // When
        final ResultActions results = mvc.perform(
                post("/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isConflict());
        assertEquals(space, spaceRepository.findById(id).orElseThrow());
    }

    @Test
    void spaceIsRegisteredCorrectly() throws Exception {
        // Given
        final String spaceName = "test-%s".formatted(Instant.now().toEpochMilli());
        final Space.Name name = new Space.Name(spaceName);
        final Space.Model model = new Space.Model("openai", "text-embedding-3-small");
        final Space.Id id = new Space.Id(name, model);
        final Space space = new Space(name, model);
        final String body =
                """
                    {
                        "name": "%s",
                        "model": {
                            "provider": "openai",
                            "name": "text-embedding-3-small"
                        }
                    }
                """.formatted(spaceName);

        // When
        final ResultActions results = mvc.perform(
                post("/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isCreated());
        assertEquals(space, spaceRepository.findById(id).orElseThrow());
    }

    @Test
    void spaceIsDeleted() throws Exception {
        // Given
        final String name = "test-%s".formatted(Instant.now().toEpochMilli());
        final String provider = "openai";
        final String model = "text-embedding-3-small";
        final Space.Name spaceName = new Space.Name(name);
        final Space.Model spaceModel = new Space.Model(provider, model);
        final Space.Id id = new Space.Id(spaceName, spaceModel);
        final String body =
                """
                    {
                        "name": "%s",
                        "model": {
                            "provider": "%s",
                            "name": "%s"
                        }
                    }
                """.formatted(name, provider, model);

        final ResultActions registerResult = mvc.perform(
                post("/spaces").contentType(MediaType.APPLICATION_JSON).content(body)
        );

        registerResult.andExpect(status().isCreated());

        // When
        final ResultActions deleteResult = mvc.perform(
                delete("/spaces/%s/providers/%s/models/%s".formatted(name, provider, model))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        deleteResult.andExpect(status().isNoContent());
        final Space space = spaceRepository.findById(id).orElseThrow();
        assertEquals(Space.Status.DELETED, space.status());
    }
}
