package ar.com.dno.ai.rag.controlplane.spaces.domain;


import java.util.Optional;


public interface SpaceRepository {
    Optional<Space> findBy(Space.Id id);
    void save(Space space);
}
