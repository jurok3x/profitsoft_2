package com.ykotsiuba.profitsoft_2.validation.annotation;

import com.ykotsiuba.profitsoft_2.validation.SearchRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SearchRequestValidator.class)
public @interface ValidSearchRequest {
    String message() default "at least one search parameter should be present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
