package ru.tomsknipineft.services;

import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для всех календарей объектов проектирования
 */
public interface GroupObjectCalendarService {

    void createCalendar(List<Integer> getDurationsProject, String codeContract, LocalDate startContract, Integer humanFactor,
                        boolean fieldEngineeringSurvey, boolean engineeringSurveyReport, Integer drillingRig, DataFormProject dataFormProject);


}
