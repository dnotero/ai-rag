package ar.com.dno.ai.rag.controlplane.namespaces.domain;


import java.util.Optional;


public interface NamespaceRepository {
    Optional<Namespace> findBy(Namespace.Name name);
    void save(Namespace namespace);
}
