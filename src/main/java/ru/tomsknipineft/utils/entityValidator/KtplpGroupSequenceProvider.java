package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.Ktplp;

import java.util.ArrayList;
import java.util.List;

public class KtplpGroupSequenceProvider implements DefaultGroupSequenceProvider<Ktplp> {
    @Override
    public List<Class<?>> getValidationGroups(Ktplp ktplp) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Ktplp.class);
        if (ktplp != null) {
            if (ktplp.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}
