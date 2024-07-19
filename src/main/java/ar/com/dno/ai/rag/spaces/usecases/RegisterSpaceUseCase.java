package ar.com.dno.ai.rag.spaces.usecases;


import ar.com.dno.ai.rag.spaces.domain.Space;
import ar.com.dno.ai.rag.spaces.domain.SpaceRepository;
import ar.com.dno.ai.rag.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class RegisterSpaceUseCase {
    private final SpaceRepository spaceRepository;


    public void handle(Request request) {
        final Space.Id spaceId = request.spaceId();
        final Optional<Space> optionalSpace = spaceRepository.findById(spaceId);

        if(optionalSpace.isPresent()) {
            throw new SpaceAlreadyExistsException(request.name(), request.model());
        }

        final Space space = new Space(request.name(), request.model());
        spaceRepository.save(space);
    }


    public record Request(Space.Name name, Space.Model model) {
        public Space.Id spaceId() {
            return new Space.Id(this.name, this.model);
        }
    }
}
