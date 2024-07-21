package ar.com.dno.ai.rag.controlplane.commons;


public enum Criticality {
    TEST,
    LOW,
    MEDIUM,
    HIGH;


    public static Criticality of(String criticality) {
        return valueOf(criticality.toUpperCase());
    }
}
