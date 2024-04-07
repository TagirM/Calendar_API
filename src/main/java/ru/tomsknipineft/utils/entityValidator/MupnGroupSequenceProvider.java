package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.oilPad.Mupn;

import java.util.ArrayList;
import java.util.List;

public class MupnGroupSequenceProvider implements DefaultGroupSequenceProvider<Mupn> {
    @Override
    public List<Class<?>> getValidationGroups(Mupn mupn) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Mupn.class);
        if (mupn != null) {
            if (mupn.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
