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
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/linear_object/linear_pipeline")
public class LinearPipelineCalendarController {

    private String codeContract;

    private boolean fieldEngineeringSurvey;

    private boolean engineeringSurveyReport;

    private DataFormLinearObjects dataFormLinearObjects;

    protected List<Calendar> calendars;

    private static final Logger logger = LogManager.getLogger(BackfillWellCalendarController.class);

    /**
     * Страница с вводом данных по линейному трубопроводу для формирования календарного плана договора
     */
    @GetMapping
    public String linearPipelinePage(Model model){
        calendars =null;
        model.addAttribute("dataFormLinearObjects", new DataFormLinearObjects());
        return "linear-pipeline";
    }

    /**
     * Получение данных из страницы ввода данных для формирования календарного плана
     * @param dataFormLinearObjects класс, содержащий основные данные по объекту инженерная подготовка кустовой площадки
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @PostMapping("/create")
    public String createCalendar(@Valid @ModelAttribute("dataFormLinearObjects") DataFormLinearObjects dataFormLinearObjects, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "linear-pipeline";
        }
        List<Integer> durationsProject = backFillWellCalendarService.getDurationOilPad(dataFormLinearObjects.getBackfillWell(),
                dataFormLinearObjects.getRoad(), dataFormLinearObjects.getLine(),
                dataFormLinearObjects.getMupn(), dataFormLinearObjects.getVec(), dataFormLinearObjects.getVvp(), dataFormLinearObjects.getCableRack(),
                dataFormLinearObjects.getVjk());
        LocalDate date = dataFormLinearObjects.getStartContract();
        this.codeContract = dataFormLinearObjects.getCodeContract();
        this.fieldEngineeringSurvey = dataFormLinearObjects.isFieldEngineeringSurvey();
        this.engineeringSurveyReport = dataFormLinearObjects.isEngineeringSurveyReport();
        if (fieldEngineeringSurvey){
            engineeringSurveyReport = true;
        }
        backFillWellCalendarService.createCalendar(durationsProject, codeContract, date, dataFormLinearObjects.getHumanFactor(),
                fieldEngineeringSurvey, engineeringSurveyReport, dataFormLinearObjects.getDrillingRig(), dataFormLinearObjects);
        this.dataFormLinearObjects = dataFormLinearObjects;

        return "redirect:/linear_object/linear_pipeline/calendar";
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
        model.addAttribute("dataFormLinearObjects", dataFormLinearObjects);
        model.addAttribute("fieldEngineeringSurvey", fieldEngineeringSurvey);
        model.addAttribute("engineeringSurveyReport", engineeringSurveyReport);
        return "result-calendar";
    }
}
