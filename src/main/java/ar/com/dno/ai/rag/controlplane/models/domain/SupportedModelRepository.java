package ar.com.dno.ai.rag.controlplane.models.domain;


import java.util.Optional;


public interface SupportedModelRepository {
    Optional<SupportedModel> findById(SupportedModel.Id id);
    void save(SupportedModel model);
}
