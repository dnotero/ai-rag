package ar.com.dno.ai.rag.spaces.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@ToString
public final class Space {
    @EqualsAndHashCode.Include
    private final Id id;
    private final Space.Status status;


    public Space(final Space.Name name, final Space.Model model) {
        this.id = new Id(name, model);
        this.status = Status.CREATED;
    }

    public Name name() {
        return this.id.name();
    }

    public Model model() {
        return this.id.model();
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
