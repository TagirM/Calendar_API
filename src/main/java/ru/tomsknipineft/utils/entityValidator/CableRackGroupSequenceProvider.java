package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.CableRack;

import java.util.ArrayList;
import java.util.List;

public class CableRackGroupSequenceProvider implements DefaultGroupSequenceProvider<CableRack> {
    @Override
    public List<Class<?>> getValidationGroups(CableRack cableRack) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(CableRack.class);
        if (cableRack != null) {
            if (cableRack.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
