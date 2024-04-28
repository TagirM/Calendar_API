package ru.tomsknipineft.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StartCalendarController {

    private final CalendarService calendarService;

    private static final Logger logger = LogManager.getLogger(StartCalendarController.class);

    /**
     * Первоначальная страница приложения с выбором типа объекта проектирования
     */
    @GetMapping
    public String index(){
        return "index";
    }

    /**
     * Страница с выбором объекта проектирования кустовой площадки
     */
    @GetMapping("/oil_pad_object")
    public String oilPadObject(){
        return "oil-pad-object";
    }

    /**
     * Страница с выбором объекта проектирования технологической площадки
     */
    @GetMapping("/tech_object")
    public String techObject(){
        return "tech-object";
    }

    /**
     * Страница с выбором объекта проектирования линейного объекта
     */
    @GetMapping("/linear_object")
    public String linearObject(){
        return "linear-object";
    }

    /**
     * Страница с выбором объекта проектирования объекта энергетики
     */
    @GetMapping("/energy_facility")
    public String energyFacility(){
        return "energy-facility";
    }

    /**
     * Получение шифра договора из страницы ввода данных для формирования календарного плана и проверка его наличия в БД
     * @param codeContract искомый шифр договора для вывода календаря
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @GetMapping("/codeContract")
    public String outputCalendar(@RequestParam String codeContract, HttpServletRequest request){
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        if (calendars.size() == 0){
            throw new  NoSuchCalendarException("Календарь по указанному шифру " + codeContract + " отсутствует в базе данных");
        }
        request.setAttribute("codeContract", codeContract);
        if (calendarService.getDataFormProject(calendars).getClass() == DataFormOilPad.class){
            return "forward:/oil_pad_object/backfill_well/calendar";
        }
        else if (calendarService.getDataFormProject(calendars).getClass() == DataFormLinearObjects.class){
            return "forward:/linear_object/linear_pipeline/calendar";
        }
        return null;
    }
}
