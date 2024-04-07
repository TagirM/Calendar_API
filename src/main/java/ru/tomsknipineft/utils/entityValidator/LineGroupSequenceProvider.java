package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.Line;

import java.util.ArrayList;
import java.util.List;

public class LineGroupSequenceProvider implements DefaultGroupSequenceProvider<Line> {
    @Override
    public List<Class<?>> getValidationGroups(Line line) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Line.class);
        if (line != null) {
            if (line.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
