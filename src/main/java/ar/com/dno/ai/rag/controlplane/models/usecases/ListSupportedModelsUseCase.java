package ar.com.dno.ai.rag.controlplane.models.usecases;


import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModel;
import ar.com.dno.ai.rag.controlplane.models.domain.SupportedModelSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class ListSupportedModelsUseCase {
    private final SupportedModelSearchService searchService;


    public List<SupportedModel> handle(ListSupportedModelsUseCase.Query query) {
        return searchService.list();
    }

    public record Query() {
    }
}
