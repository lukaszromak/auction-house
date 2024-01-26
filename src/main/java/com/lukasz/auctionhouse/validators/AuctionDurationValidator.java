package com.lukasz.auctionhouse.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

public class AuctionDurationValidator implements ConstraintValidator<AuctionDuration, Date> {

    private static final long millisInDay = 1000l*60l*60l*24l;

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        long diff = new Date(date.getTime() - new Date().getTime()).getTime();

        return diff > millisInDay;
    }
}
