package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class GetSpaceUseCase {
    private final SpaceSearchService spaceSearchService;


    public Optional<Space> handle(GetSpaceUseCase.Query query) {
        return spaceSearchService.findBy(query.spaceId());
    }


    public record Query(Space.Id spaceId) {
    }
}
