package ru.tomsknipineft.utils.entityValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.tomsknipineft.entities.linearObjects.Line;

public class LineValidationValidator implements ConstraintValidator<LineValidation, Line> {
    @Override
    public void initialize(LineValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Line line, ConstraintValidatorContext constraintValidatorContext) {
        if (line.isActive()) {
            return line.getLength() != null;
        }
        return true;
    }
}
