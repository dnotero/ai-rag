package ar.com.dno.ai.rag.controlplane.spaces.adapters.output.memory;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceSearchService;
import org.springframework.stereotype.Repository;

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
    public Optional<Space> findBy(Space.Id id) {
        return Optional.ofNullable(spacesById.get(id));
    }

    @Override
    public synchronized void save(final Space space) {
        spacesById.put(space.id(), space);
        spacesByNameAndModel.computeIfAbsent(space.name(), name -> new HashMap<>()).put(space.model(), space);
    }

    @Override
    public List<Space> findBy(Space.Name name) {
        return spacesById.values().stream()
                .filter(space -> space.name().equals(name))
                .toList();
    }

    @Override
    public List<Space> findBy(Space.Name name, Space.Status status) {
        return spacesById.values().stream()
                .filter(space -> space.name().equals(name))
                .filter(space -> space.status() == status)
                .toList();
    }

    @Override
    public Optional<Space> findBy(Space.Id id, Space.Status status) {
        return Optional.ofNullable(spacesById.get(id))
                .filter(space -> space.status() == status);
    }

    @Override
    public List<Space> findAllBy(Space.Model model, Space.Status status) {
        return spacesById.values().stream()
                .filter(space -> space.model().equals(model))
                .toList();
    }
}
