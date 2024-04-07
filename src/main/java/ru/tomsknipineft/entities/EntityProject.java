package ru.tomsknipineft.entities;

/**
 * Интерефейс всех объектов проектирования
 */
public interface EntityProject {

    Integer getStage();

    void setStage(Integer stage);

    ObjectType getObjectType();

    void setObjectType(ObjectType objectType);
    boolean isActive();
}
