package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinearLineGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final CalendarService calendarService;

    private final PipelineService pipelineService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private static final Logger logger = LogManager.getLogger(LinearLineGroupCalendarServiceImpl.class);

    @Override
    public Integer resourceStage(EntityProject entityProject) {
        return null;
    }

    @Override
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjects) {
        return null;
    }
}
