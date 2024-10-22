package com.hungdoan.aquariux.common.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CryptoPairValidator.class)
public @interface CryptoPairValid {
    String message() default "Invalid crypto pair. Must be 'ETHUSDT' or 'BTCUSDT'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}