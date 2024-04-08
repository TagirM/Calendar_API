package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.ObjectType;
import ru.tomsknipineft.entities.areaObjects.Vec;
import ru.tomsknipineft.entities.areaObjects.Vjk;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.entities.oilPad.Mupn;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Класс с бизнес-логикой расчета сроков календарного плана договора из входных данных
 */

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final BackfillWellService backfillWellService;

    private final MupnService mupnService;

    private final VecService vecService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private final VjkService vjkService;

    private final DataFormProjectService dataFormProjectService;

    private static final Logger logger = LogManager.getLogger(CalendarService.class);

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
     */
    public void createCalendar(List<Integer> getDurationsProject, String codeContract, LocalDate startContract, Integer humanFactor,
                               boolean fieldEngineeringSurvey, boolean engineeringSurveyReport, Integer drillingRig, DataFormOilPad dataFormOilPad) {
        //  запись в файл данных о проекте
        this.dataFormProjectService.dataFormProjectSave(dataFormOilPad);
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
            engineeringSurveyReportDuration = engineeringSurveyDuration + engineeringSurveyLaboratoryResearch + 45;
            agreementEngineeringSurveyDuration = engineeringSurveyReportDuration + 60;
        }
        // проверка условия что полевые ИИ не выполняются, а камеральные ИИ выполняются
        if (!fieldEngineeringSurvey && engineeringSurveyReport) {
            engineeringSurveyReportDuration = 45;
            agreementEngineeringSurveyDuration = engineeringSurveyReportDuration + 60;
        }

        for (int i = 0; i < getDurationsProject.size(); i++) {
            // расчет количества рабочих дней РД с учетом человеческого фактора
            int durationsProjectWithHumanFactor = (getDurationsProject.get(i) * (humanFactor + 100)) / 100 + projectOfficeDays;

            // расчет календарных дней РД из рабочих дней в каждом этапе строительства
            int calendarDaysDurationsProject = durationsProjectWithHumanFactor + (durationsProjectWithHumanFactor / 5) * 2;

            Calendar calendar = new Calendar();
            // смещение начала текущего этапа строительства на срок полевых ИИ предыдущего этапа строительства
            startContract = startContract.plusDays(nextStage);

            //  расчет продолжительности стадий проекта
            //  срок выдачи РД от начала работ
            int workingDuration = calendarDaysDurationsProject + engineeringSurveyReportDuration;
            // срок выдачи ПД от начала работ
            int projectDuration = workingDuration + 30;
            // срок выдачи СД от начала работ
            int estimatesDuration = workingDuration + 30;
            // срок выдачи ЗУР от начала работ
            int landDuration = projectDuration + 150;
            // срок согласования РД от начала работ
            int agreementWorkingDuration = workingDuration + 60;
            // срок согласования ПД от начала работ
            int agreementProjectDuration = projectDuration + 60;
            // срок согласования СД от начала работ
            int agreementEstimatesDuration = estimatesDuration + 60;
            // срок ГГЭ ПД от начала работ
            int examinationDuration = agreementProjectDuration + 120;

            // дата начала отчета ИИ текущего этапа строительства
            LocalDate startEngineeringSurveyReport = workDay(startContract.plusDays(engineeringSurveyDuration));
            // проверка условия пересечения выполнения отчета ИИ текущего этапа строительства с предыдущим, если пересечение есть, то срок сместить
            if (finishEngineeringSurveyReport != null && startEngineeringSurveyReport.plusDays(engineeringSurveyLaboratoryResearch).isBefore(finishEngineeringSurveyReport)) {
                Period period = Period.between(startEngineeringSurveyReport.plusDays(engineeringSurveyLaboratoryResearch), finishEngineeringSurveyReport);
                // количество дней смещения
                stageOffsetII = period.getDays() + period.getMonths() * 30;
            }
            // дата окончания отчета ИИ текущего этапа строительства
            finishEngineeringSurveyReport = workDay(startContract.plusDays(engineeringSurveyReportDuration + stageOffsetII));

            // дата начала РД текущего этапа строительства
            LocalDate startWorking = workDay(startContract.plusDays(engineeringSurveyReportDuration+ stageOffsetII));
            // проверка условия пересечения выполнения РД текущего этапа строительства с предыдущим, если пересечение есть, то срок сместить
            if (finishWorking != null && startWorking.isBefore(finishWorking)) {
                Period period = Period.between(startWorking, finishWorking);
                // количество дней смещения
                stageOffsetPSD = stageOffsetPSD + period.getDays() + period.getMonths() * 30;
            }
            // дата окончания РД текущего этапа строительства
            finishWorking = workDay(startContract.plusDays(workingDuration + stageOffsetII + stageOffsetPSD));

            // формирование календаря проекта
            try {
                calendar.setCodeContract(codeContract).setStartContract(workDay(startContract))
                        .setStage(i + 1)
                        .setEngineeringSurvey(workDay(startContract.plusDays(engineeringSurveyDuration)))
                        .setEngineeringSurveyReport(workDay(startContract.plusDays(engineeringSurveyReportDuration + stageOffsetII)))
                        .setAgreementEngineeringSurvey(workDay(startContract.plusDays(agreementEngineeringSurveyDuration + stageOffsetII)))
                        .setWorkingStart(workDay(startContract.plusDays(engineeringSurveyReportDuration + stageOffsetII + stageOffsetPSD)))
                        .setWorkingFinish(workDay(startContract.plusDays(workingDuration + stageOffsetII + stageOffsetPSD)))

                        .setEstimatesFinish(workDay(startContract.plusDays(estimatesDuration + stageOffsetII + stageOffsetPSD)))
                        .setProjectFinish(workDay(startContract.plusDays(projectDuration + stageOffsetII + stageOffsetPSD)))
                        .setLandFinish(workDay(startContract.plusDays(landDuration + stageOffsetII + stageOffsetPSD)))
                        .setAgreementWorking(workDay(startContract.plusDays(agreementWorkingDuration + stageOffsetII + stageOffsetPSD)))
                        .setAgreementProject(workDay(startContract.plusDays(agreementProjectDuration + stageOffsetII + stageOffsetPSD)))
                        .setAgreementEstimates(workDay(startContract.plusDays(agreementEstimatesDuration + stageOffsetII + stageOffsetPSD)))
                        .setExamination(workDay(startContract.plusDays(examinationDuration + stageOffsetII + stageOffsetPSD)))
                        .setHumanFactor(humanFactor)
                        .setBytesDataProject(Files.readAllBytes(Paths.get(dataFormProjectService.getFilePathSave())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // проверка наличия в базе предыдущих календарей по данному шифру, если есть, то удалить, чтобы не возникало конфликта календарей
            if (i == 0 && calendarRepository.findCalendarByCodeContract(codeContract).isPresent()) {
                calendarRepository.deleteAll(getCalendarByCode(codeContract));
            }
            calendarRepository.save(calendar);
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
    }

    /**
     * Получение списка сроков проектирования объекта по этапам его строительства
     * @param backfillWell Кустовая площадка
     * @param road         Автодорога
     * @param line         ЛЭП
     * @param mupn         площадка МУПН
     * @param vec          ВЭЦ
     * @param vvp          Временная вертолетная площадка
     * @return список сроков проектирования объекта по этапам его строительства
     */
    public List<Integer> getDurationOilPad(BackfillWell backfillWell, Road road, Line line, Mupn mupn, Vec vec, Vvp vvp, CableRack cableRack, Vjk vjk) {
        List<EntityProject> objects = listActiveEntityProject(List.of(backfillWell, road, line, mupn, vec, vvp, cableRack, vjk));

        List<Integer> durationsProject = new ArrayList<>();

        int stage = defineStageProject(objects);
        Map<Integer, Integer> divisionDurationByStage = new HashMap<>();
        for (EntityProject oilPad :
                objects) {
            if (oilPad.getObjectType().equals(ObjectType.AREA)) {
                if (divisionDurationByStage.containsKey(oilPad.getStage())) {
                    divisionDurationByStage.put(oilPad.getStage(), divisionDurationByStage.get(oilPad.getStage()) + resourceStageOilPad(oilPad));
                } else {
                    divisionDurationByStage.put(oilPad.getStage(), resourceStageOilPad(oilPad));
                }
            }
        }
        for (EntityProject oilPad :
                objects) {
            if (oilPad.getObjectType().equals(ObjectType.LINEAR)) {
                if (!divisionDurationByStage.containsKey(oilPad.getStage())) {
                    divisionDurationByStage.put(oilPad.getStage(), resourceStageOilPad(oilPad));
                } else {
                    if (divisionDurationByStage.get(oilPad.getStage()) < resourceStageOilPad(oilPad)) {
                        divisionDurationByStage.put(oilPad.getStage(), resourceStageOilPad(oilPad));
                    }
                }
            }
        }
        for (int i = 1; i <= stage; i++) {
            durationsProject.add(divisionDurationByStage.get(i));
        }
        return durationsProject;
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
        logger.info("Количество этапов в проекте определено и равно " + stage);
        return stage;
    }

    /**
     * Получение количества ресурса, необходимого для проектирования сущности (сооружения) всего объекта проектирования
     * @param oilPad сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    public Integer resourceStageOilPad(EntityProject oilPad) {
        int durationStage = 0;
        if (oilPad.getClass() == BackfillWell.class) {
            durationStage += backfillWellService.getResourceBackfillWell((BackfillWell) oilPad);

        } else if (oilPad.getClass() == Road.class) {
            durationStage += roadService.getResourceRoad((Road) oilPad);

        } else if (oilPad.getClass() == Line.class) {
            durationStage += lineService.getResourceLine((Line) oilPad);

        } else if (oilPad.getClass() == Mupn.class) {
            durationStage += mupnService.getResourceMupn((Mupn) oilPad);

        } else if (oilPad.getClass() == Vec.class) {
            durationStage += vecService.getResourceVec((Vec) oilPad);
        } else if (oilPad.getClass() == Vvp.class) {
            durationStage += vvpService.getResourceVvp((Vvp) oilPad);
        }
        return durationStage;
    }

    /**
     * Получение списка только активных сущностей (сооружений) объекта проектирования из представления
     * @param entityProjects сущность (сооружение) объекта проектирования
     * @return список активных сущностей (сооружений)
     */
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjects) {
        List<EntityProject> objects = new ArrayList<>();
        for (EntityProject entity :
                entityProjects) {
            if (entity.isActive()) {
                if (entity.getStage() == null) {
                    entity.setStage(1);
                }
                if (entity.getClass() == BackfillWell.class) {
                    entity.setObjectType(backfillWellService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Road.class) {
                    entity.setObjectType(roadService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Line.class) {
                    entity.setObjectType(lineService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Mupn.class) {
                    entity.setObjectType(mupnService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Vec.class) {
                    entity.setObjectType(vecService.getFirst().getObjectType());
                    ((Vec) entity).setPower(vecService.getFirst().getPower());
                    objects.add(entity);
                } else if (entity.getClass() == Vvp.class) {
                    entity.setObjectType(vvpService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == CableRack.class) {
                    entity.setObjectType(cableRackService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Vjk.class) {
                    entity.setObjectType(vjkService.getFirst().getObjectType());
                    objects.add(entity);
                }
            }
        }
        return objects;
    }

    /**
     * Метод, учитывающий выходные и праздничные дни. При попадании даты на выходной, производится перенос на будний день
     * @param date исходная дата
     * @return будний день
     */
    public LocalDate workDay(LocalDate date) {
        Collection<DayOfWeek> weekends = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        Collection<LocalDate> holidays = new HashSet<>(List.of(
                LocalDate.of(date.getYear(), 1, 1),
                LocalDate.of(date.getYear(), 1, 2),
                LocalDate.of(date.getYear(), 1, 3),
                LocalDate.of(date.getYear(), 1, 4),
                LocalDate.of(date.getYear(), 1, 5),
                LocalDate.of(date.getYear(), 1, 6),
                LocalDate.of(date.getYear(), 1, 7),
                LocalDate.of(date.getYear(), 1, 8),
                LocalDate.of(date.getYear(), 2, 23),
                LocalDate.of(date.getYear(), 3, 8),
                LocalDate.of(date.getYear(), 5, 1),
                LocalDate.of(date.getYear(), 5, 9),
                LocalDate.of(date.getYear(), 6, 12),
                LocalDate.of(date.getYear(), 11, 4)));

        while (weekends.contains(date.getDayOfWeek()) || holidays.contains(date)) {
            date = date.plusDays(1);
        }
        return date;
    }

    /**
     * Метод, учитывающий крайний день календаря 10е число в декабре и 20е число в остальных месяцах.
     * Так как сроки актирования этапа работ с 1 по 10 число в декабре, с 1 по 20 - в другие месяцы.
     * Если дата выходит за рамка указанных дат, то перенос на следующий месяц (если 21 - то на 1е число, если 22 - на 2е число и т.д.)
     * @param date исходная дата
     * @return валидный день для актирования
     */
    public LocalDate checkDeadlineForActivation(LocalDate date) {

        return date;
    }
}
