package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinearPipelineGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final CalendarService calendarService;

    @Override
    public List<Integer> getDuration(List<EntityProject> entityProject) {
        return null;
    }

    @Override
    public Integer resourceStage(EntityProject oilPad) {
        return null;
    }

    @Override
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjects) {
        return null;
    }
}
