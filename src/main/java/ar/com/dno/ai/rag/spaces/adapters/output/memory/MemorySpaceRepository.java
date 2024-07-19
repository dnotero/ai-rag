package ar.com.dno.ai.rag.spaces.adapters.output.memory;


import ar.com.dno.ai.rag.spaces.domain.Space;
import ar.com.dno.ai.rag.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.spaces.domain.SpaceSearchService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class MemorySpaceRepository implements SpaceRepository, SpaceSearchService {
    private final Map<Space.Id, Space> spacesById;
    private final Map<Space.Name, Map<Space.Model, Space>> spacesByNameAndModel;


    public MemorySpaceRepository() {
        this(new HashMap<>(), new HashMap<>());
    }

    public MemorySpaceRepository(Map<Space.Id, Space> spacesById,
                                 Map<Space.Name, Map<Space.Model, Space>> spacesByNameAndModel) {
        this.spacesById = new HashMap<>(spacesById);
        this.spacesByNameAndModel = new HashMap<>(spacesByNameAndModel);
    }

    @Override
    public Optional<Space> findById(Space.Id id) {
        return Optional.ofNullable(spacesById.get(id));
    }

    @Override
    public synchronized void save(final Space space) {
        spacesById.put(space.id(), space);
        spacesByNameAndModel.computeIfAbsent(space.name(), name -> new HashMap<>()).put(space.model(), space);
    }

    @Override
    public List<Space> findByName(Space.Name name) {
        return new ArrayList<>(spacesByNameAndModel.getOrDefault(name, Map.of()).values());
    }

    @Override
    public Optional<Space> findByNameAndModel(Space.Name name, Space.Model model) {
        return Optional.ofNullable(spacesByNameAndModel.getOrDefault(name, Map.of()).get(model));
    }
}
