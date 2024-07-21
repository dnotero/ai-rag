package ar.com.dno.ai.rag.controlplane.namespaces.adapters.output.memory;


import ar.com.dno.ai.rag.controlplane.namespaces.domain.Namespace;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceRepository;
import ar.com.dno.ai.rag.controlplane.namespaces.domain.NamespaceSearchService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class MemoryNamespaceRepository implements NamespaceRepository, NamespaceSearchService {
    private final Map<Namespace.Name, Namespace> db;


    public MemoryNamespaceRepository() {
        this(new HashMap<>());
    }

    public MemoryNamespaceRepository(Map<Namespace.Name, Namespace> db) {
        this.db = new ConcurrentHashMap<>(db);
    }

    @Override
    public Optional<Namespace> findBy(Namespace.Name name) {
        return Optional.ofNullable(db.get(name));
    }

    @Override
    public void save(Namespace namespace) {
        db.put(namespace.name(), namespace);
    }

    @Override
    public List<Namespace> findAll() {
        return new ArrayList<>(db.values());
    }
}
