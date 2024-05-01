package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;

import java.util.ArrayList;
import java.util.List;

public class EngineeringSurveyLinearObjectsGroupSequenceProvider  implements DefaultGroupSequenceProvider<DataFormLinearObjects> {
    @Override
    public List<Class<?>> getValidationGroups(DataFormLinearObjects dataFormLinearObjects) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(DataFormLinearObjects.class);
        if (dataFormLinearObjects != null) {
            if (dataFormLinearObjects.isFieldEngineeringSurvey()) {
                groups.add(OnActiveEngineeringSurvey.class);
            }
        }
        return groups;
    }
}
