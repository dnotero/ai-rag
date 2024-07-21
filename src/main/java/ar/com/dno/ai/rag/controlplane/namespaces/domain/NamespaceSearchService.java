package ar.com.dno.ai.rag.controlplane.namespaces.domain;


import java.util.List;


public interface NamespaceSearchService {
    List<Namespace> findAll();
}
