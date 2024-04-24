package ru.tomsknipineft.entities;

import ru.tomsknipineft.entities.enumEntities.ObjectType;

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
