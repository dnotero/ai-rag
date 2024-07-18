package ar.com.dno.ai.embeddings.spaces.adapters.output.memory;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceRepository;
import ar.com.dno.ai.embeddings.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;


@Repository
public class MemorySpaceRepository implements SpaceRepository {
    private final Map<Space.Name, Map<Space.Model, Space>> db;


    public MemorySpaceRepository() {
        this(new HashMap<>());
    }

    public MemorySpaceRepository(Map<Space.Name, Map<Space.Model, Space>> db) {
        this.db = new HashMap<>(db);
    }

    @Override
    public Optional<Space> findByNameAndModel(Space.Name name, Space.Model model) {
        return Optional.ofNullable(db.getOrDefault(name, Map.of()).get(model));
    }

    @Override
    public synchronized void save(final Space space) {
        db.computeIfAbsent(space.name(), name -> new HashMap<>())
                .compute(space.model(), (model, dbSpace) -> doSave(space, dbSpace));
    }

    private static Space doSave(Space space, Space dbSpace) {
        if (nonNull(dbSpace)) {
            throw new SpaceAlreadyExistsException(space.name(), space.model());
        }

        return space;
    }
}
