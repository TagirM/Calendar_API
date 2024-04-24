package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class PipelineGroupSequenceProvider implements DefaultGroupSequenceProvider<Pipeline> {
    @Override
    public List<Class<?>> getValidationGroups(Pipeline pipeline) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Pipeline.class);
        if (pipeline != null) {
            if (pipeline.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
