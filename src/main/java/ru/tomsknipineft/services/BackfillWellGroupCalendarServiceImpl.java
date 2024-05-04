package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.entities.oilPad.BackfillWell;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс с бизнес-логикой расчета сроков календарного плана договора из входных данных
 */

@Service
@RequiredArgsConstructor
public class BackfillWellGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final BackfillWellService backfillWellService;

    private final BackfillSiteService backfillSiteService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    public Integer resourceStage(EntityProject entityProjectBackfillWell) {
        int durationStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            durationStage += backfillWellService.getResourceForWorkDocBackfillWell((BackfillWell) entityProjectBackfillWell);

        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            durationStage += roadService.getResourceRoad((Road) entityProjectBackfillWell);

        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            durationStage += lineService.getResourceLine((Line) entityProjectBackfillWell);

        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            durationStage += backfillSiteService.getResourceForWorkDocBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            durationStage += vvpService.getResourceVvp((Vvp) entityProjectBackfillWell);
        }
        return durationStage;
    }

    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjectsBackfillWell) {
        List<EntityProject> objects = new ArrayList<>();
        for (EntityProject entity :
                entityProjectsBackfillWell) {
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
