package ar.com.dno.ai.embeddings.spaces.domain;


import java.util.Optional;


public interface SpaceRepository {
    Optional<Space> findByNameAndModel(Space.Name name, Space.Model model);
    void save(Space space);
}
