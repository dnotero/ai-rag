package ar.com.dno.ai.rag.controlplane.models.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;


@Accessors(fluent = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter(onMethod = @__(@JsonProperty))
@ToString
public class SupportedModel {
    @EqualsAndHashCode.Include
    private final SupportedModel.Id id;
    private final SupportedModel.Status status;
    private final Set<InputFormat> supportedFormats;
    private final JsonNode metadata;


    public SupportedModel(Provider provider, Name name, Set<InputFormat> supportedFormats, JsonNode metadata) {
        this(new Id(provider, name), supportedFormats, metadata);
    }

    public SupportedModel(SupportedModel.Id id, Set<InputFormat> supportedFormats, JsonNode metadata) {
        this(id, Status.ENABLED, supportedFormats, metadata);
    }

    public SupportedModel.Provider provider() {
        return this.id.provider();
    }

    public SupportedModel.Name name() {
        return this.id.name();
    }

    public SupportedModel deprecate() {
        return new SupportedModel(this.id, Status.DEPRECATED, this.supportedFormats, this.metadata);
    }

    public SupportedModel metadata(JsonNode metadata) {
        return new SupportedModel(this.id, this.status, this.supportedFormats, metadata);
    }

    public boolean isDeprecated() {
        return Status.DEPRECATED == this.status;
    }


    public record Id(SupportedModel.Provider provider, SupportedModel.Name name) {
    }

    public record Provider(String value) {
        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }
    }


    public record Name(String value) {
        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }
    }


    public enum Status {
        ENABLED,
        DEPRECATED;
    }

    public enum InputFormat {
        TEXT,
        JSON,
        IMAGE,
        AUDIO,
        VIDEO;
    }
}
