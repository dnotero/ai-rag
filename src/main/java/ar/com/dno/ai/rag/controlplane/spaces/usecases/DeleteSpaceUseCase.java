package ar.com.dno.ai.rag.controlplane.spaces.usecases;


import ar.com.dno.ai.rag.controlplane.spaces.domain.Space;
import ar.com.dno.ai.rag.controlplane.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class DeleteSpaceUseCase {
    private final SpaceRepository spaceRepository;


    @Transactional
    public void handle(DeleteSpaceUseCase.Request request) {
        final Space.Id spaceId = new Space.Id(request.name(), request.model());
        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceNotFoundException(spaceId));

        final Space disabledSpace = space.markAsDelete();

        spaceRepository.save(disabledSpace);
    }


    public record Request(Space.Name name, Space.Model model) {
    }
}
