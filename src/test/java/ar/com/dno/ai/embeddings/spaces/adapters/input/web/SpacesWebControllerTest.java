package ar.com.dno.ai.embeddings.spaces.adapters.input.web;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceRepository;
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
class SpacesWebControllerTest {
    @Autowired
    private MockMvc mvs;
    @Autowired
    private SpaceRepository spaceRepository;


    @Test
    void noDuplicateSpacesAllowed() throws Exception {
        // Given
        final String spaceName = "test-%s".formatted(Instant.now().toEpochMilli());
        final Space.Name name = new Space.Name(spaceName);
        final Space.Model model = new Space.Model("openai", "text-embedding-3-small");
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
        final ResultActions results = mvs.perform(
                post("/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isConflict());
        assertEquals(space, spaceRepository.findByNameAndModel(name, model).orElseThrow());
    }

    @Test
    void spaceIsRegisteredCorrectly() throws Exception {
        // Given
        final String spaceName = "test-%s".formatted(Instant.now().toEpochMilli());
        final Space.Name name = new Space.Name(spaceName);
        final Space.Model model = new Space.Model("openai", "text-embedding-3-small");
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
        final ResultActions results = mvs.perform(
                post("/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isCreated());
        assertEquals(space, spaceRepository.findByNameAndModel(name, model).orElseThrow());
    }

}
