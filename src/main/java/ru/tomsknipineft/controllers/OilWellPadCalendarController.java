package ru.tomsknipineft.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.BackfillWellCalendarService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oil_pad_object")
public class OilWellPadCalendarController {
    private final BackfillWellCalendarService backFillWellCalendarService;

    private String codeContract;

    private boolean fieldEngineeringSurvey;

    private boolean engineeringSurveyReport;

    private DataFormOilPad dataFormOilPad;

    private static final Logger logger = LogManager.getLogger(OilWellPadCalendarController.class);


    /**
     * Страница с вводом данных по обустройству куста для формирования календарного плана договора
     */
    @GetMapping("/oil_well_pad")
    public String oilWellPad(Model model){
//        model.addAttribute("dataFormOilPad", new DataFormOilPad());
        return "oil-well-pad";
    }

    /**
     * Страница с вводом данных по реконструкции куста для формирования календарного плана договора
     */
    @GetMapping("/reconstruction_oil_well_pad")
    public String reconstructionOilWellPad(Model model){
//        model.addAttribute("dataFormOilPad", new DataFormOilPad());
        return "reconstruction-oil-well-pad";
    }
}
