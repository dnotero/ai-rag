package ar.com.dno.ai.rag.controlplane.spaces.domain;


import ar.com.dno.ai.rag.controlplane.commons.Criticality;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.spaces.domain.exceptions.NamespaceAlreadyAssignedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import static java.util.Objects.nonNull;


@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter(onMethod = @__(@JsonProperty))
@ToString
public final class Space {
    @EqualsAndHashCode.Include
    private final Id id;
    private final Space.Status status;
    private final Criticality criticality;
    private final Namespace.Name namespace;


    public Space(final Space.Name name, final Space.Model model, final Criticality criticality) {
        this(new Id(name, model), Status.CREATED, criticality, null);
    }

    private Space(final Space.Id id, final Space.Status status, final Criticality criticality, final Namespace.Name namespace) {
        this.id = id;
        this.status = status;
        this.criticality = criticality;
        this.namespace = namespace;
    }

    public Name name() {
        return this.id.name();
    }

    public Model model() {
        return this.id.model();
    }

    public Space markAsDisabled() {
        return new Space(this.id, Status.DISABLED, this.criticality, this.namespace);
    }

    public Space markAsDelete() {
        return new Space(this.id, Status.DELETED, this.criticality, this.namespace);
    }

    public Space assign(Namespace namespace) {
        if (nonNull(this.namespace)) {
            throw new NamespaceAlreadyAssignedException(this.id);
        }
        return new Space(this.id, this.status, this.criticality, namespace.name());
    }


    public record Id(Name name, Model model) {
    }


    public record Name(String value) {
        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }
    }


    public record Model(String provider, String name) {
        public SupportedModel.Id toSupportedModelId() {
            return new SupportedModel.Id(new SupportedModel.Provider(this.provider), new SupportedModel.Name(this.name));
        }
    }


    public enum Status {
        CREATED,
        ACTIVE,
        DISABLED,
        DELETED;
    }
}
