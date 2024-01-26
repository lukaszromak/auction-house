package com.lukasz.auctionhouse.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuctionDurationValidator.class)
public @interface AuctionDuration {

    String message() default "Minimum auction duration is one day.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
