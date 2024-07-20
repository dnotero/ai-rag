package ar.com.dno.ai.rag.controlplane.models.domain;


import java.util.List;
import java.util.Optional;


public interface SupportedModelSearchService {
    List<SupportedModel> list();
    Optional<SupportedModel> findBy(SupportedModel.Id id);
}
