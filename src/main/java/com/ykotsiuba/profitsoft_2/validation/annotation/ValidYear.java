package com.ykotsiuba.profitsoft_2.validation.annotation;

import com.ykotsiuba.profitsoft_2.validation.YearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearValidator.class)
public @interface ValidYear {

    String message() default "invalid year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
