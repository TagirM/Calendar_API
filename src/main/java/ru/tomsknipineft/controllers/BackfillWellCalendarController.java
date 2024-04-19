package ru.tomsknipineft.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.BackfillWellCalendarService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oil_pad_object/backfill_well")
public class BackfillWellCalendarController {

    private final BackfillWellCalendarService backFillWellCalendarService;

    private String codeContract;

    private boolean fieldEngineeringSurvey;

    private boolean engineeringSurveyReport;

    private DataFormOilPad dataFormOilPad;


    protected List <Calendar> calendars;

    private static final Logger logger = LogManager.getLogger(BackfillWellCalendarController.class);

    /**
     * Страница с вводом данных по инженерной подготовке куста для формирования календарного плана договора
     */
    @GetMapping
    public String backfillWellPage(Model model){
        calendars =null;
        model.addAttribute("dataFormOilPad", new DataFormOilPad());
        return "backfill-well";
    }

    /**
     * Получение данных из страницы ввода данных для формирования календарного плана
     * @param dataFormOilPad класс, содержащий основные данные по объекту инженерная подготовка кустовой площадки
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @PostMapping("/create")
    public String createCalendar(@Valid @ModelAttribute("dataFormOilPad") DataFormOilPad dataFormOilPad, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "backfill-well";
        }
        List<Integer> durationsProject = backFillWellCalendarService.getDurationOilPad(dataFormOilPad.getBackfillWell(),
                dataFormOilPad.getRoad(), dataFormOilPad.getLine(),
                dataFormOilPad.getMupn(), dataFormOilPad.getVec(), dataFormOilPad.getVvp(), dataFormOilPad.getCableRack(), dataFormOilPad.getVjk());
        LocalDate date = dataFormOilPad.getStartContract();
        this.codeContract = dataFormOilPad.getCodeContract();
        this.fieldEngineeringSurvey = dataFormOilPad.isFieldEngineeringSurvey();
        this.engineeringSurveyReport = dataFormOilPad.isEngineeringSurveyReport();
        if (fieldEngineeringSurvey){
            engineeringSurveyReport = true;
        }
        backFillWellCalendarService.createCalendar(durationsProject, codeContract, date, dataFormOilPad.getHumanFactor(),
                fieldEngineeringSurvey, engineeringSurveyReport, dataFormOilPad.getDrillingRig(), dataFormOilPad);
        this.dataFormOilPad = dataFormOilPad;

        return "redirect:/oil_pad_object/backfill_well/calendar";
    }

    /**
     * Страница с выводом календарного плана договора
     */
    @GetMapping("/calendar")
    public String resultCalendar(Model model){
        if (calendars == null){
            calendars = backFillWellCalendarService.getCalendarByCode(codeContract);
            logger.info("Календарь найденный по шифру " + codeContract + " - " + calendars);
        }
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormOilPad", dataFormOilPad);
        model.addAttribute("fieldEngineeringSurvey", fieldEngineeringSurvey);
        model.addAttribute("engineeringSurveyReport", engineeringSurveyReport);
        return "result-calendar";
    }
}

