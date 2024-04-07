package ru.tomsknipineft.controllers;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StartCalendarController {

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
}
