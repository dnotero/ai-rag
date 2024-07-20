package ar.com.dno.ai.rag.controlplane.models.adapters.output.memory;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelRepository;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelSearchService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class MemorySupportedModelRepository implements SupportedModelRepository, SupportedModelSearchService {
    private final Map<SupportedModel.Id, SupportedModel> db;


    public MemorySupportedModelRepository() {
        this(new ConcurrentHashMap<>());
    }

    public MemorySupportedModelRepository(Map<SupportedModel.Id, SupportedModel> db) {
        this.db = new ConcurrentHashMap<>(db);
    }

    @Override
    public Optional<SupportedModel> findBy(SupportedModel.Id id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<SupportedModel> list() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void save(SupportedModel model) {
        db.put(model.id(), model);
    }
}
