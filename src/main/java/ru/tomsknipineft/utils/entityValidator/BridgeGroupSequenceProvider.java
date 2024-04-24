package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.Bridge;

import java.util.ArrayList;
import java.util.List;

public class BridgeGroupSequenceProvider implements DefaultGroupSequenceProvider<Bridge> {
    @Override
    public List<Class<?>> getValidationGroups(Bridge bridge) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Bridge.class);
        if (bridge != null) {
            if (bridge.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
