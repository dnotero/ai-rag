package ar.com.dno.ai.rag.controlplane.spaces.domain.selectors;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.spaces.domain.NamespaceSelector;
import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class CriticalityNamespaceSelector implements NamespaceSelector {
    @Override
    public Optional<Namespace> select(Space space, List<Namespace> namespaces) {
        return namespaces.stream()
                .filter(namespace -> namespace.criticality() == space.criticality())
                .findFirst();
    }
}
