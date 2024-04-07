package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.Road;

import java.util.ArrayList;
import java.util.List;

public class RoadGroupSequenceProvider implements DefaultGroupSequenceProvider<Road> {
    @Override
    public List<Class<?>> getValidationGroups(Road road) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Road.class);
        if (road != null) {
            if (road.isActive()) {
                groups.add(OnActiveCheck.class);
                if (road.isBridgeExist()) {
                    groups.add(OnActiveBridgeRoad.class);
                }
            }
        }
        return groups;
    }
}
