package ar.com.dno.ai.rag.controlplane.commons.domain;


import com.fasterxml.jackson.annotation.JsonCreator;


public enum Criticality {
    TEST,
    LOW,
    MEDIUM,
    HIGH;


    @JsonCreator
    public static Criticality of(String criticality) {
        return valueOf(criticality.toUpperCase());
    }
}
