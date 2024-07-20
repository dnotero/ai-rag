package ar.com.dno.ai.rag.controlplane.models.domain;


import java.util.List;


public interface SupportedModelSearchService {
    List<SupportedModel> list();
}
