package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Mps;

import java.util.ArrayList;
import java.util.List;

public class MpsGroupSequenceProvider implements DefaultGroupSequenceProvider<Mps> {
    @Override
    public List<Class<?>> getValidationGroups(Mps mps) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Mps.class);
        if (mps != null) {
            if (mps.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}