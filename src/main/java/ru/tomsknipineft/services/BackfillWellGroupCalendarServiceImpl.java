package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.entities.oilPad.BackfillWell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс с бизнес-логикой расчета сроков календарного плана договора из входных данных
 */

@Service
@RequiredArgsConstructor
public class BackfillWellGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final CalendarService calendarService;

    private final BackfillWellService backfillWellService;

    private final BackfillSiteService backfillSiteService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);


    public List<Integer> getDuration(List<EntityProject> entityProjects) {
        List<EntityProject> objects = listActiveEntityProject(entityProjects);

        List<Integer> durationsProject = new ArrayList<>();

        int stage = calendarService.defineStageProject(objects);
        Map<Integer, Integer> divisionDurationByStage = new HashMap<>();
        for (EntityProject oilPad :
                objects) {
            if (oilPad.getObjectType().equals(ObjectType.AREA)) {
                if (divisionDurationByStage.containsKey(oilPad.getStage())) {
                    divisionDurationByStage.put(oilPad.getStage(), divisionDurationByStage.get(oilPad.getStage()) + resourceStage(oilPad));
                } else {
                    divisionDurationByStage.put(oilPad.getStage(), resourceStage(oilPad));
                }
            }
        }
        for (EntityProject oilPad :
                objects) {
            if (oilPad.getObjectType().equals(ObjectType.LINEAR)) {
                if (!divisionDurationByStage.containsKey(oilPad.getStage())) {
                    divisionDurationByStage.put(oilPad.getStage(), resourceStage(oilPad));
                } else {
                    if (divisionDurationByStage.get(oilPad.getStage()) < resourceStage(oilPad)) {
                        divisionDurationByStage.put(oilPad.getStage(), resourceStage(oilPad));
                    }
                }
            }
        }
        for (int i = 1; i <= stage; i++) {
            durationsProject.add(divisionDurationByStage.get(i));
        }
        return durationsProject;
    }

    public Integer resourceStage(EntityProject oilPad) {
        int durationStage = 0;
        if (oilPad.getClass() == BackfillWell.class) {
            durationStage += backfillWellService.getResourceBackfillWell((BackfillWell) oilPad);

        } else if (oilPad.getClass() == Road.class) {
            durationStage += roadService.getResourceRoad((Road) oilPad);

        } else if (oilPad.getClass() == Line.class) {
            durationStage += lineService.getResourceLine((Line) oilPad);

        } else if (oilPad.getClass() == BackfillSite.class) {
            durationStage += backfillSiteService.getResourceBackfillSite((BackfillSite) oilPad);
        } else if (oilPad.getClass() == Vvp.class) {
            durationStage += vvpService.getResourceVvp((Vvp) oilPad);
        }
        return durationStage;
    }

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
                } else if (entity.getClass() == BackfillSite.class) {
                    entity.setObjectType(backfillSiteService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Vvp.class) {
                    entity.setObjectType(vvpService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == CableRack.class) {
                    entity.setObjectType(cableRackService.getFirst().getObjectType());
                    objects.add(entity);
                }
            }
        }
        return objects;
    }
}
