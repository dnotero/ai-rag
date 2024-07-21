package ar.com.dno.ai.rag.controlplane.namespaces.domain;


import ar.com.dno.ai.rag.controlplane.commons.Criticality;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Namespace {
    @EqualsAndHashCode.Include
    private final Namespace.Name name;
    private final Criticality criticality;


    public record Name(String value) {
        @Override
        public String toString() {
            return this.value;
        }
    }
}
