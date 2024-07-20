package ar.com.dno.ai.rag.controlplane.models.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter(onMethod = @__(@JsonProperty))
@ToString
public class SupportedModel {
    @EqualsAndHashCode.Include
    private final SupportedModel.Id id;
    private final SupportedModel.Status status;
    private final JsonNode metadata;


    public SupportedModel(Provider provider, Name name, JsonNode metadata) {
        this(new Id(provider, name), metadata);
    }

    public SupportedModel(SupportedModel.Id id, JsonNode metadata) {
        this(id, Status.ENABLED, metadata);
    }

    public SupportedModel.Provider provider() {
        return this.id.provider();
    }

    public SupportedModel.Name name() {
        return this.id.name();
    }

    public SupportedModel deprecate() {
        return new SupportedModel(this.id, Status.DEPRECATED, this.metadata);
    }

    public SupportedModel metadata(JsonNode metadata) {
        return new SupportedModel(this.id, this.status, metadata);
    }


    public record Id(SupportedModel.Provider provider, SupportedModel.Name name) {
    }

    public record Provider(String value) {
    }


    public record Name(String value) {
    }


    public enum Status {
        ENABLED,
        DEPRECATED,
        DELETED;
    }
}
