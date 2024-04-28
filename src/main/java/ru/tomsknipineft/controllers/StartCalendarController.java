package ru.tomsknipineft.controllers;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StartCalendarController {

    private final CalendarService calendarService;

    private List <Calendar> calendars;

    private String codeContract;

    private DataFormOilPad dataFormOilPad;

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
     * Получение данных из страницы ввода данных для формирования календарного плана
     * @param code искомый шифр договора для вывода календаря
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @PostMapping("/code")
    public String outputCalendar(@RequestParam String code){
        calendars = calendarService.getCalendarByCode(code);
        if (calendars.size() == 0){
            throw new  NoSuchCalendarException("Календарь по указанному шифру " + code + " отсутствует в базе данных");
        }
        codeContract = code;
        dataFormOilPad = (DataFormOilPad) calendarService.getDataFormProject(calendars);
        return "redirect:/calendar";
    }

    /**
     * Страница с выводом календарного плана договора по шифру
     */
    @GetMapping("/calendar")
    public String findCalendar(Model model){
        logger.info("Календарь, найденный по шифру " + codeContract + " - " + calendars);
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormOilPad", dataFormOilPad);
        model.addAttribute("fieldEngineeringSurvey", dataFormOilPad.isFieldEngineeringSurvey());
        model.addAttribute("engineeringSurveyReport", dataFormOilPad.isEngineeringSurveyReport());
        return "result-calendar";
    }
}
