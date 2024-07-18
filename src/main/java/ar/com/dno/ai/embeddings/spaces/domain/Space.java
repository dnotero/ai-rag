package ar.com.dno.ai.embeddings.spaces.domain;


import lombok.EqualsAndHashCode;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public final class Space {
    private final Id id;


    public Space(final Space.Name name, final Space.Model model) {
        this.id = new Id(name, model);
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
}
