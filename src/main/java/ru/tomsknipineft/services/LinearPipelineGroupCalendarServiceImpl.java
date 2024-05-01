package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Pipeline;
import ru.tomsknipineft.entities.linearObjects.Road;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinearPipelineGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final PipelineService pipelineService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private static final Logger logger = LogManager.getLogger(LinearPipelineGroupCalendarServiceImpl.class);

    @Override
    public Integer resourceStage(EntityProject entityProjectLinearPipeline) {
        int durationStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            durationStage += pipelineService.getResourcePipeline((Pipeline) entityProjectLinearPipeline);

        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            durationStage += roadService.getResourceRoad((Road) entityProjectLinearPipeline);

        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            durationStage += lineService.getResourceLine((Line) entityProjectLinearPipeline);

        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            durationStage += vvpService.getResourceVvp((Vvp) entityProjectLinearPipeline);
        }
        return durationStage;
    }

    @Override
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjectsLinearPipeline) {
        List<EntityProject> objects = new ArrayList<>();
        for (EntityProject entity :
                entityProjectsLinearPipeline) {
            if (entity.isActive()) {
                if (entity.getStage() == null) {
                    entity.setStage(1);
                }
                if (entity.getClass() == Pipeline.class) {
                    entity.setObjectType(pipelineService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Road.class) {
                    entity.setObjectType(roadService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Line.class) {
                    entity.setObjectType(lineService.getFirst().getObjectType());
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
