package ar.com.dno.ai.rag.controlplane.spaces.domain;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;

import java.util.List;
import java.util.Optional;


public interface NamespaceSelector {
    Optional<Namespace> select(Space space, List<Namespace> namespaces);
}
