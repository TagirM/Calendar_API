package ru.tomsknipineft.services;

import ru.tomsknipineft.entities.EntityProject;

import java.util.List;

/**
 * Интерфейс для всех календарей объектов проектирования
 */
public interface GroupObjectCalendarService {

    /**
     * Получение списка сроков проектирования объекта по этапам его строительства
     * @param entityProject лист сооружений проектируемого объекта
     * @return список сроков проектирования объекта по этапам его строительства
     */
    List<Integer> getDuration(List<EntityProject> entityProject);

    /**
     * Получение количества ресурса, необходимого для проектирования сущности (сооружения) всего объекта проектирования
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceStage(EntityProject entityProject);


    /**
     * Получение списка только активных сущностей (сооружений) объекта проектирования из представления
     * @param entityProjects сущность (сооружение) объекта проектирования
     * @return список активных сущностей (сооружений)
     */
    List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjects);
}
