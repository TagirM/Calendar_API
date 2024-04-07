package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Vjk;
import ru.tomsknipineft.entities.areaObjects.Vvp;

import java.util.ArrayList;
import java.util.List;

public class VvpGroupSequenceProvider implements DefaultGroupSequenceProvider<Vvp> {
    @Override
    public List<Class<?>> getValidationGroups(Vvp vvp) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Vvp.class);
        if (vvp != null) {
            if (vvp.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
