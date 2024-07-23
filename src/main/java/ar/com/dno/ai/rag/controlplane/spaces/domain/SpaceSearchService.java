package ar.com.dno.ai.rag.controlplane.spaces.domain;


import java.util.List;
import java.util.Optional;


public interface SpaceSearchService {
    List<Space> findBy(Space.Name name);
    List<Space> findBy(Space.Name name, Space.Status status);
    Optional<Space> findBy(Space.Id id);
    Optional<Space> findBy(Space.Id id, Space.Status status);
    List<Space> findAllBy(Space.Model model, Space.Status status);
}
