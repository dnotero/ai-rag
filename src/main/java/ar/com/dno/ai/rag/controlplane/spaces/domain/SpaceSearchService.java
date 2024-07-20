package ar.com.dno.ai.rag.controlplane.spaces.domain;


import java.util.List;
import java.util.Optional;


public interface SpaceSearchService {
    List<Space> findByName(Space.Name name);
    Optional<Space> findByNameAndModel(Space.Name name, Space.Model model);
}
