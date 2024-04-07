package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Vec;
import ru.tomsknipineft.entities.areaObjects.Vjk;

import java.util.ArrayList;
import java.util.List;

public class VecGroupSequenceProvider implements DefaultGroupSequenceProvider<Vec> {
    @Override
    public List<Class<?>> getValidationGroups(Vec vec) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Vec.class);
        if (vec != null) {
            if (vec.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
