package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.entities.areaObjects.Vec;

import java.util.ArrayList;
import java.util.List;

public class SiknGroupSequenceProvider  implements DefaultGroupSequenceProvider<Sikn> {
    @Override
    public List<Class<?>> getValidationGroups(Sikn sikn) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Sikn.class);
        if (sikn != null) {
            if (sikn.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
