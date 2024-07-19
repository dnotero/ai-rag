package ar.com.dno.ai.rag.spaces.usecases;


import ar.com.dno.ai.rag.spaces.domain.Space;
import ar.com.dno.ai.rag.spaces.domain.SpaceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class ListSpacesUseCase {
    private final SpaceSearchService spaceSearchService;


    public List<Space> handle(ListSpacesUseCase.Query query) {
        return spaceSearchService.findByName(query.name());
    }

    public record Query(Space.Name name) {
    }
}
