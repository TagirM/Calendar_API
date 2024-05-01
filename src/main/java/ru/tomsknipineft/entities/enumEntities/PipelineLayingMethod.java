package ru.tomsknipineft.entities.enumEntities;

/**
 * Способ прокладка трубопровода: подземный, наземный, надземный (на эстакаде)
 */
public enum PipelineLayingMethod {

    UNDERGROUND("подземный"), GROUND("наземный"), ABOVEGROUND("надземный на эстакаде");

    private final String title;

    PipelineLayingMethod(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
