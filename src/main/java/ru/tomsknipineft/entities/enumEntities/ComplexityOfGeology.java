package ru.tomsknipineft.entities.enumEntities;

/**
 * Сложность прокладка линейных коммуникаций: сложная, средняя, легкая
 */
public enum ComplexityOfGeology {
    DIFFICULT("сложная геология"), MEDIUM("геология средней сложности"), EASY("простая геология");

    private final String title;

    ComplexityOfGeology(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
