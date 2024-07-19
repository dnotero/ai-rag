package ar.com.dno.ai.rag.spaces.usecases;


import ar.com.dno.ai.rag.spaces.domain.Space;
import ar.com.dno.ai.rag.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.spaces.usecases.exceptions.SpaceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class DisableSpaceUseCase {
    private final SpaceRepository spaceRepository;


    public void handle(DisableSpaceUseCase.Request request) {
        final Space.Id spaceId = new Space.Id(request.name(), request.model());
        final Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceNotFoundException(spaceId));

        final Space disabledSpace = space.markAsDisabled();

        spaceRepository.save(disabledSpace);
    }


    public record Request(Space.Name name, Space.Model model) {
    }
}
