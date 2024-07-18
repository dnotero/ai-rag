package ar.com.dno.ai.embeddings.spaces.domain;


import java.util.List;


public interface SpaceSearchService {
    List<Space> findByName(Space.Name name);
}
