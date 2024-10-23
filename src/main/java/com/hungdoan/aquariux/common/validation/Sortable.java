package com.hungdoan.aquariux.common.validation;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sortable {
    String message() default "This field is support sorting";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}