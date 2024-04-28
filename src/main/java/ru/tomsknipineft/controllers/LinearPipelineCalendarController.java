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
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.services.LinearPipelineGroupCalendarServiceImpl;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/linear_object/linear_pipeline")
public class LinearPipelineCalendarController {

    private final LinearPipelineGroupCalendarServiceImpl linearObjectGroupCalendarService;
    private final CalendarService calendarService;
    private String codeContract;

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
        List<EntityProject> entityProjects = List.of(dataFormLinearObjects.getPipeline(),
                dataFormLinearObjects.getRoad(), dataFormLinearObjects.getBridge(), dataFormLinearObjects.getLine(),
                dataFormLinearObjects.getSikn(), dataFormLinearObjects.getMps(), dataFormLinearObjects.getKtplp(),
                dataFormLinearObjects.getVvp(), dataFormLinearObjects.getCableRack());
        List<Integer> durationsProject = linearObjectGroupCalendarService.getDuration(entityProjects);

        LocalDate date = dataFormLinearObjects.getStartContract();
        this.codeContract = dataFormLinearObjects.getCodeContract();
        if (dataFormLinearObjects.isFieldEngineeringSurvey()){
            dataFormLinearObjects.setEngineeringSurveyReport(true);
        }
        calendars = calendarService.createCalendar(durationsProject, codeContract, date, dataFormLinearObjects.getHumanFactor(),
                dataFormLinearObjects.isFieldEngineeringSurvey(), dataFormLinearObjects.isEngineeringSurveyReport(),
                dataFormLinearObjects.getDrillingRig(), dataFormLinearObjects);
        this.dataFormLinearObjects = dataFormLinearObjects;

        return "redirect:/linear_object/linear_pipeline/calendar";
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
            dataFormLinearObjects = (DataFormLinearObjects) calendarService.getDataFormProject(calendars);
        }
        logger.info("Календарь по шифру " + codeContract + " выведен - " + calendars);
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormLinearObjects", dataFormLinearObjects);
        model.addAttribute("fieldEngineeringSurvey", dataFormLinearObjects.isFieldEngineeringSurvey());
        model.addAttribute("engineeringSurveyReport", dataFormLinearObjects.isEngineeringSurveyReport());
        return "result-calendar";
    }
}
