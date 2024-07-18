package ar.com.dno.ai.embeddings.spaces.usecases;


import ar.com.dno.ai.embeddings.spaces.domain.Space;
import ar.com.dno.ai.embeddings.spaces.domain.SpaceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class GetSpaceUseCase {
    private final SpaceSearchService spaceSearchService;


    public Optional<Space> query(GetSpaceUseCase.Query query) {
        return spaceSearchService.findByNameAndModel(query.name(), query.model());
    }

    public record Query(Space.Name name, Space.Model model) {
    }
}
