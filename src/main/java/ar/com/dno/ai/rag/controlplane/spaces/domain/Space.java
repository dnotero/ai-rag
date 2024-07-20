package ar.com.dno.ai.rag.controlplane.spaces.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter(onMethod = @__(@JsonProperty))
@ToString
public final class Space {
    @EqualsAndHashCode.Include
    private final Id id;
    private final Space.Status status;


    public Space(final Space.Name name, final Space.Model model) {
        this(new Id(name, model), Status.CREATED);
    }

    private Space(final Space.Id id, final Space.Status status) {
        this.id = id;
        this.status = status;
    }

    public Name name() {
        return this.id.name();
    }

    public Model model() {
        return this.id.model();
    }

    public Space markAsDisabled() {
        return new Space(this.id, Status.DISABLED);
    }

    public Space markAsDelete() {
        return new Space(this.id, Status.DELETED);
    }


    public record Id(Name name, Model model) {
    }


    public record Name(String value) {
    }


    public record Model(String provider, String name) {
    }


    public enum Status {
        CREATED,
        ACTIVE,
        DISABLED,
        DELETED;
    }
}
