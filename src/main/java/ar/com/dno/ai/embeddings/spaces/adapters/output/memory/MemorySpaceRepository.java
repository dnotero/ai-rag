package ar.com.dno.ai.embeddings.spaces.adapters.output.memory;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceRepository;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceSearchService;
import ar.com.dno.ai.embeddings.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;


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
        spacesById.compute(space.id(), (id, dbSpace) -> doSave(space, dbSpace));
        spacesByNameAndModel.computeIfAbsent(space.name(), name -> new HashMap<>())
                .compute(space.model(), (model, dbSpace) -> doSave(space, dbSpace));
    }

    private static Space doSave(Space space, Space dbSpace) {
        if (nonNull(dbSpace)) {
            throw new SpaceAlreadyExistsException(space.name(), space.model());
        }

        return space;
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
