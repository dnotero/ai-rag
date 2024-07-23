package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class DisableSpaceUseCase {
    private final SpaceRepository spaceRepository;


    @Transactional
    public void handle(DisableSpaceUseCase.Request request) {
        final Space space = spaceRepository.findBy(request.spaceId())
                .orElseThrow(() -> new SpaceNotFoundException(request.spaceId()));

        final Space disabledSpace = space.markAsDisabled();

        spaceRepository.save(disabledSpace);
    }


    public record Request(Space.Id spaceId) {
    }
}
