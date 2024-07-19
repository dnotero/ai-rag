package ar.com.dno.ai.rag.spaces.domain;


import java.util.Optional;


public interface SpaceRepository {
    Optional<Space> findById(Space.Id id);
    void save(Space space);
}
