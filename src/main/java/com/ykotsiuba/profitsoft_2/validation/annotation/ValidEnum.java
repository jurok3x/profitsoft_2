package com.ykotsiuba.profitsoft_2.validation.annotation;

import com.ykotsiuba.profitsoft_2.validation.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "invalid enum value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
