package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;

import java.util.ArrayList;
import java.util.List;

public class BackfillSiteGroupSequenceProvider implements DefaultGroupSequenceProvider<BackfillSite> {
    @Override
    public List<Class<?>> getValidationGroups(BackfillSite backfillSite) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(BackfillSite.class);
        if (backfillSite != null) {
            if (backfillSite.isActive()) {
                groups.add(OnActiveCheck.class);
            }
        }
        return groups;
    }
}