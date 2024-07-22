package ar.com.dno.ai.rag.controlplane.namespaces.domain;


import java.util.List;
import java.util.Optional;


public interface NamespaceSearchService {
    List<Namespace> findAll();
    Optional<Namespace> findBy(Namespace.Name name);
}
