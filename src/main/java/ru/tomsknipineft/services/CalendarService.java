package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final DateService dateService;

    private final DataFormProjectService dataFormProjectService;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    /**
     * Получение всего списка календарных планов (различных этапов строительства) по шифру договора
     * @param code шифр договора
     * @return список календарных планов по различным этапам строительства
     */
    public List<Calendar> getCalendarByCode(String code) {
        return calendarRepository.findCalendarByCodeContract(code)
                .orElseThrow(() -> new NoSuchCalendarException("Календарь по указанному шифру " + code + " отсутствует в базе данных"));
    }

    /**
     * Метод получения данных проекта из базы данных
     * @param calendars календарь проекта
     * @return данные проекта
     */
    public DataFormProject getDataFormProject(List<Calendar> calendars){
        DataFormProject dataFormProject;
        try {
            FileOutputStream f = new FileOutputStream(dataFormProjectService.getFilePathRecover());
            f.write(calendars.get(0).getBytesDataProject());
            f.close();
            dataFormProject = dataFormProjectService.dataFormProjectRecover();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataFormProject;
    }

    /**
     * Создание календарного плана договора с учетом всех этапов строительства
     * @param getDurationsProject список продолжительности проектирования всех этапов строительтсва по договору
     * @param codeContract шифр договора
     * @param startContract дата начала работ
     * @param humanFactor человеческий фактор
     * @param fieldEngineeringSurvey наличие полевых ИИ
     * @param engineeringSurveyReport наличие камеральных ИИ
     * @param drillingRig количество буровых бригад
     * @param dataFormProject исходные данные проекта
     */
    public List<Calendar> createCalendar(List<Integer> getDurationsProject, String codeContract, LocalDate startContract, Integer humanFactor,
                               boolean fieldEngineeringSurvey, boolean engineeringSurveyReport, Integer drillingRig, DataFormProject dataFormProject) {
        List<Calendar> calendars =new ArrayList<>();
        //  запись в файл данных о проекте
        this.dataFormProjectService.dataFormProjectSave(dataFormProject);
        // переменные метода
        int nextStage = 0;
        int engineeringSurveyDuration = 0;
        int engineeringSurveyLaboratoryResearch = 0;
        int engineeringSurveyReportDuration = 0;
        int agreementEngineeringSurveyDuration = 0;
        LocalDate finishEngineeringSurveyReport = null;
        LocalDate finishWorking = null;
        int stageOffsetII = 0;
        int stageOffsetPSD = 0;
        // количество дней необходимых офису для сбора и передачи документации заказчику с учетом всех процедур
        int projectOfficeDays = 3;

        // проверка условия что полный комплекс ИИ выполняется
        if (fieldEngineeringSurvey) {
            engineeringSurveyDuration = 10 + 20 / drillingRig;
            engineeringSurveyLaboratoryResearch = 45;
            engineeringSurveyReportDuration = 45;
            agreementEngineeringSurveyDuration = 60;
        }
        // проверка условия что полевые ИИ не выполняются, а камеральные ИИ выполняются
        if (!fieldEngineeringSurvey && engineeringSurveyReport) {
            engineeringSurveyReportDuration = 45;
            agreementEngineeringSurveyDuration = 60;
        }

        for (int i = 0; i < getDurationsProject.size(); i++) {
            // расчет количества рабочих дней РД с учетом человеческого фактора
            int durationsProjectWithHumanFactor = (getDurationsProject.get(i) * (humanFactor + 100)) / 100 + projectOfficeDays;

            // перерасчет рабочих дней в календарные дни РД в каждом этапе строительства
            int calendarDaysDurationsProject = durationsProjectWithHumanFactor + (durationsProjectWithHumanFactor / 5) * 2;

            Calendar calendar = new Calendar();
            // смещение начала текущего этапа строительства на срок полевых ИИ предыдущего этапа строительства при этом
            // приводим дату начала работ к рабочему дню (если выпал праздничный или выходной день)
            startContract = dateService.workDay(startContract.plusDays(nextStage));

            //  расчет продолжительности стадий проекта
            //  срок разработки РД
            int workingDuration = calendarDaysDurationsProject;
            // срок разработки ПД
            int projectDuration = 30;
            // срок разработки СД
            int estimatesDuration = 30;
            // срок разработки ЗУР
            int landDuration = 120;
            // срок согласования РД
            int agreementWorkingDuration = 60;
            // срок согласования ПД
            int agreementProjectDuration = 60;
            // срок согласования СД
            int agreementEstimatesDuration = 60;
            // срок ГГЭ ПД
            int examinationProjectDuration = 120;
            // дата окончания полевых ИИ текущего этапа строительства
            LocalDate finishEngineeringSurvey = dateService.workDay(startContract.plusDays(engineeringSurveyDuration));
            // дата начала отчета ИИ текущего этапа строительства
            LocalDate startEngineeringSurveyReport = finishEngineeringSurvey;
            // проверка условия пересечения выполнения отчета ИИ текущего этапа строительства с предыдущим, если пересечение есть, то срок сместить
            if (finishEngineeringSurveyReport != null && startEngineeringSurveyReport.plusDays(engineeringSurveyLaboratoryResearch).isBefore(finishEngineeringSurveyReport)) {
                Period period = Period.between(startEngineeringSurveyReport.plusDays(engineeringSurveyLaboratoryResearch), finishEngineeringSurveyReport);
                // количество дней смещения
                stageOffsetII = period.getDays() + period.getMonths() * 30;
            }
            // дата окончания отчета ИИ текущего этапа строительства
            finishEngineeringSurveyReport = dateService.workDay(startEngineeringSurveyReport.plusDays(engineeringSurveyLaboratoryResearch +
                    engineeringSurveyReportDuration + stageOffsetII));
            // дата окончания согласования отчета ИИ текущего этапа строительства
            LocalDate finishAgreementEngineeringSurveyReport = dateService.workDay(finishEngineeringSurveyReport.plusDays(agreementEngineeringSurveyDuration));

            // дата начала РД текущего этапа строительства
            LocalDate startWorking = finishEngineeringSurveyReport;
            // проверка условия пересечения выполнения РД текущего этапа строительства с предыдущим, если пересечение есть, то срок сместить
            if (finishWorking != null && startWorking.isBefore(finishWorking)) {
                Period period = Period.between(startWorking, finishWorking);
                // количество дней смещения
                stageOffsetPSD = period.getDays() + period.getMonths() * 30;
            }
            // дата окончания РД текущего этапа строительства = дате начала разработки смет и дате начала проектной документации
            finishWorking = dateService.workDay(startWorking.plusDays(workingDuration + stageOffsetPSD));
            // дата окончания согласования РД текущего этапа строительства
            LocalDate finishAgreementWorking = dateService.workDay(finishWorking.plusDays(agreementWorkingDuration));
            // дата окончания разработки СД текущего этапа строительства
            LocalDate estimatesFinish = dateService.workDay(finishWorking.plusDays(estimatesDuration));
            // дата окончания согласования СД текущего этапа строительства
            LocalDate agreementEstimatesFinish = dateService.workDay(estimatesFinish.plusDays(agreementEstimatesDuration));
            // дата окончания разработки ПД текущего этапа строительства
            LocalDate projectFinish = dateService.workDay(finishWorking.plusDays(projectDuration));
            // дата окончания согласования ПД текущего этапа строительства
            LocalDate agreementProjectFinish = dateService.workDay(projectFinish.plusDays(agreementProjectDuration));
            // дата окончания разработки ЗУР текущего этапа строительства
            LocalDate landFinish =  dateService.workDay(projectFinish.plusDays(landDuration));
            // дата окончания ГГЭ ПД текущего этапа строительства
            LocalDate examinationProjectFinish = dateService.workDay(agreementProjectFinish.plusDays(examinationProjectDuration));

            // формирование календаря проекта с проверкой попадания даты позже 10го числа в декабре и 20го числа - в остальных месяцах
            try {
                calendar.setCodeContract(codeContract).setStartContract(startContract)
                        .setStage(i + 1)
                        .setEngineeringSurvey(dateService.checkDeadlineForActivation(finishEngineeringSurvey))
                        .setEngineeringSurveyReport(dateService.checkDeadlineForActivation(finishEngineeringSurveyReport))
                        .setAgreementEngineeringSurvey(dateService.checkDeadlineForActivation(finishAgreementEngineeringSurveyReport))
                        .setWorkingStart(dateService.checkDeadlineForActivation(startWorking))
                        .setWorkingFinish(dateService.checkDeadlineForActivation(finishWorking))

                        .setEstimatesFinish(dateService.checkDeadlineForActivation(estimatesFinish))
                        .setProjectFinish(dateService.checkDeadlineForActivation(projectFinish))
                        .setLandFinish(dateService.checkDeadlineForActivation(landFinish))
                        .setAgreementWorking(dateService.checkDeadlineForActivation(finishAgreementWorking))
                        .setAgreementProject(dateService.checkDeadlineForActivation(agreementProjectFinish))
                        .setAgreementEstimates(dateService.checkDeadlineForActivation(agreementEstimatesFinish))
                        .setExamination(dateService.checkDeadlineForActivation(examinationProjectFinish))
                        .setHumanFactor(humanFactor)
                        .setBytesDataProject(Files.readAllBytes(Paths.get(dataFormProjectService.getFilePathSave())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // проверка наличия в базе предыдущих календарей по данному шифру, если есть, то удалить, чтобы не возникало конфликта календарей
            if (i == 0 && calendarRepository.findCalendarByCodeContract(codeContract).isPresent()) {
                calendarRepository.deleteAll(getCalendarByCode(codeContract));
            }
            calendars.add(calendarRepository.save(calendar));
            logger.info("Создан новый календарь " + calendar);
            // обнуление смещения начала работ для следующего этапа строительтсва, чтобы для следующего этапа расчет смещения начался заново
            nextStage = 0;
            // расчет смещения начала работ для следующего этапа строительтсва
            if (fieldEngineeringSurvey) {
                nextStage += engineeringSurveyDuration;
            } else if (engineeringSurveyReport) {
                nextStage += engineeringSurveyReportDuration;
            } else {
                nextStage += calendarDaysDurationsProject;
            }
            // обнуления количества дней смещения отчета ИИ и РД за счет наложения этапов, для следующего этапа строительства
            stageOffsetII = 0;
            stageOffsetPSD = 0;
        }
        return calendars;
    }

        /**
     * Получение общего количества этапов строительства всего объекта проектирования по договору
     * @param entityProjects сооружение (сущность) объекта проектирования
     * @return общее количество этапов строительства объекта
     */
    public Integer defineStageProject(List<EntityProject> entityProjects) {
        int stage = 0;
        for (EntityProject entity :
                entityProjects) {
            if (entity.getStage() > stage) {
                stage = entity.getStage();
            }
        }
        logger.info("Количество этапов строительства в проекте определено и равно " + stage);
        return stage;
    }
}
