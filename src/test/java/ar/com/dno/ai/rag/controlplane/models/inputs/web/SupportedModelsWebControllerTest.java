package ar.com.dno.ai.rag.controlplane.models.inputs.web;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupportedModelsWebControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private SupportedModelRepository modelRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void noDuplicateSupportedModelsAllowed() throws Exception {
        // Given
        final SupportedModel.Provider provider = new SupportedModel.Provider("openai");
        final String supportedModelName = "text-embedding-3-small-%s".formatted(Instant.now().toEpochMilli());
        final SupportedModel.Name name = new SupportedModel.Name(supportedModelName);
        final SupportedModel.Id id = new SupportedModel.Id(provider, name);
        final JsonNode metadata = objectMapper.readTree(
                """
                    {
                        "field_1": "value_1"
                    }
                """);
        final SupportedModel model = new SupportedModel(provider, name, metadata);
        modelRepository.save(model);

        // When
        final ResultActions results = mvc.perform(
                post("/admin/providers/openai/models/%s".formatted(supportedModelName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "field_2": "value_2"
                            }
                        """)
        );

        // Then
        results.andExpect(status().isConflict());
        assertEquals(model, modelRepository.findBy(id).orElseThrow());
    }

    @Test
    void supportedModelIsRegisteredCorrectly() throws Exception {
        final SupportedModel.Provider provider = new SupportedModel.Provider("openai");
        final String supportedModelName = "text-embedding-3-small-%s".formatted(Instant.now().toEpochMilli());
        final SupportedModel.Name name = new SupportedModel.Name(supportedModelName);
        final SupportedModel.Id id = new SupportedModel.Id(provider, name);
        final String body =
                """
                    {
                        "field_1": "value_1"
                    }
                """;

        // When
        final ResultActions results = mvc.perform(
                post("/admin/providers/openai/models/%s".formatted(supportedModelName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        // Then
        results.andExpect(status().isCreated());
        final SupportedModel supportedModel = modelRepository.findBy(id).orElseThrow();
        assertEquals(provider, supportedModel.provider());
        assertEquals(name, supportedModel.name());
        assertEquals(SupportedModel.Status.ENABLED, supportedModel.status());
        assertEquals(objectMapper.readTree(body), supportedModel.metadata());
    }

    @Test
    void supportedModelIsDisabled() throws Exception {
        final SupportedModel.Provider provider = new SupportedModel.Provider("openai");
        final String supportedModelName = "text-embedding-3-small-%s".formatted(Instant.now().toEpochMilli());
        final SupportedModel.Name name = new SupportedModel.Name(supportedModelName);
        final SupportedModel.Id id = new SupportedModel.Id(provider, name);
        final JsonNode metadata = objectMapper.readTree(
                """
                    {
                        "field_1": "value_1"
                    }
                """);
        final SupportedModel model = new SupportedModel(provider, name, metadata);
        modelRepository.save(model);

        // When
        final ResultActions results = mvc.perform(
                delete("/admin/providers/openai/models/%s".formatted(supportedModelName))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        results.andExpect(status().isNoContent());
        final SupportedModel supportedModel = modelRepository.findBy(id).orElseThrow();
        assertEquals(provider, supportedModel.provider());
        assertEquals(name, supportedModel.name());
        assertEquals(SupportedModel.Status.DEPRECATED, supportedModel.status());
        assertEquals(metadata, supportedModel.metadata());
    }

    @Test
    void listSupportedModels() throws Exception {
        final SupportedModel.Provider provider = new SupportedModel.Provider("openai");
        final String supportedModelName = "text-embedding-3-small-%s".formatted(Instant.now().toEpochMilli());
        final SupportedModel.Name name = new SupportedModel.Name(supportedModelName);
        final JsonNode metadata = objectMapper.readTree(
                """
                    {
                        "field_1": "value_1"
                    }
                """);
        final SupportedModel model = new SupportedModel(provider, name, metadata);
        modelRepository.save(model);

        // When
        final ResultActions results = mvc.perform(
                get("/admin/supported_models").contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id.name.value").value(is(supportedModelName)))
                .andDo(MockMvcResultHandlers.print());;
    }

    @Test
    void supportedModelMetadataIsOverrode() throws Exception {
        final SupportedModel.Provider provider = new SupportedModel.Provider("openai");
        final String supportedModelName = "text-embedding-3-small-%s".formatted(Instant.now().toEpochMilli());
        final SupportedModel.Name name = new SupportedModel.Name(supportedModelName);
        final SupportedModel.Id id = new SupportedModel.Id(provider, name);
        final JsonNode metadata = objectMapper.readTree(
                """
                    {
                        "field_1": "value_1"
                    }
                """);
        final SupportedModel model = new SupportedModel(provider, name, metadata);
        modelRepository.save(model);

        // When
        final String newMetadata = """
                    {
                      "field_2": "value_2"
                    }
                """;
        final ResultActions results = mvc.perform(
                put("/admin/providers/openai/models/%s/metadata".formatted(supportedModelName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMetadata)
        );

        // Then
        results.andExpect(status().isNoContent());
        final SupportedModel supportedModel = modelRepository.findBy(id).orElseThrow();
        assertEquals(provider, supportedModel.provider());
        assertEquals(name, supportedModel.name());
        assertEquals(SupportedModel.Status.ENABLED, supportedModel.status());
        assertEquals(objectMapper.readTree(newMetadata), supportedModel.metadata());
    }


}
