package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Vjk;

import java.util.ArrayList;
import java.util.List;

public class VjkGroupSequenceProvider implements DefaultGroupSequenceProvider<Vjk> {
    @Override
    public List<Class<?>> getValidationGroups(Vjk vjk) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Vjk.class);
        if (vjk != null) {
            if (vjk.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}