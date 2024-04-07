package ru.tomsknipineft.entities.oilPad;


import ru.tomsknipineft.entities.ObjectType;

/**
 * Интерфейс для сооружений инженерной подготовки кустовой площадки
 */
public interface OilPad {

    ObjectType getObjectType();
    boolean isActive();

    Integer getStage();
}
