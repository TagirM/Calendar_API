package ru.tomsknipineft.controllers;

import jakarta.servlet.http.HttpServletRequest;
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
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.BackfillWellGroupCalendarServiceImpl;
import ru.tomsknipineft.services.CalendarService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oil_pad_object/backfill_well")
public class BackfillWellCalendarController {

    private final BackfillWellGroupCalendarServiceImpl backFillWellCalendarServiceImpl;

    private final CalendarService calendarService;

    private String codeContract;

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
        List<EntityProject> entityProjects = new ArrayList<>(List.of(dataFormOilPad.getBackfillWell(), dataFormOilPad.getRoad(), dataFormOilPad.getLine(),
                dataFormOilPad.getVvp(), dataFormOilPad.getCableRack()));
        entityProjects.addAll(dataFormOilPad.getBackfillSites());
        Map<Integer, Integer> durationsProject = calendarService.getDuration(entityProjects, backFillWellCalendarServiceImpl);

        if (dataFormOilPad.isFieldEngineeringSurvey()){
            dataFormOilPad.setEngineeringSurveyReport(true);
        }
        this.codeContract = dataFormOilPad.getCodeContract();
        this.dataFormOilPad = dataFormOilPad;

        calendars = calendarService.createCalendar(durationsProject, codeContract, dataFormOilPad.getStartContract(), dataFormOilPad.getHumanFactor(),
                dataFormOilPad.isFieldEngineeringSurvey(), dataFormOilPad.isEngineeringSurveyReport(), dataFormOilPad.getDrillingRig(), dataFormOilPad);

        return "redirect:/oil_pad_object/backfill_well/calendar";
    }

    /**
     * Страница с выводом календарного плана договора
     */
    @GetMapping("/calendar")
    public String resultCalendar(Model model, HttpServletRequest request){
        String codeFromRequest = (String) request.getAttribute("codeContract");
        if (codeFromRequest != null){
            codeContract = codeFromRequest;
            calendars = calendarService.getCalendarByCode(codeContract);
            dataFormOilPad = (DataFormOilPad) calendarService.getDataFormProject(calendars);
        }
        logger.info("Календарь по шифру " + codeContract + " выведен - " + calendars);
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormOilPad", dataFormOilPad);
        model.addAttribute("fieldEngineeringSurvey", dataFormOilPad.isFieldEngineeringSurvey());
        model.addAttribute("engineeringSurveyReport", dataFormOilPad.isEngineeringSurveyReport());
        return "oil-pad-result-calendar";
    }
}

