package com.ykotsiuba.profitsoft_2.validation;

import com.ykotsiuba.profitsoft_2.validation.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<ValidEnum, Number> {
    @Override
    public boolean isValid(Number number, ConstraintValidatorContext constraintValidatorContext) {
        if (number == null) {
            return true;
        }

        int currentYear = LocalDate.now().getYear();
        int year = number.intValue();

        return year <= currentYear;
    }
}
