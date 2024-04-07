package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;

import java.util.ArrayList;
import java.util.List;

public class EngineeringSurveyGroupSequenceProvider implements DefaultGroupSequenceProvider<DataFormOilPad> {
    @Override
    public List<Class<?>> getValidationGroups(DataFormOilPad dataFormOilPad) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(DataFormOilPad.class);
        if (dataFormOilPad != null) {
            if (dataFormOilPad.isFieldEngineeringSurvey()) {
                groups.add(OnActiveEngineeringSurvey.class);
            }
        }
        return groups;
    }
}
