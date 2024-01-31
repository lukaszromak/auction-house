package com.lukasz.auctionhouse.validators;

import com.lukasz.auctionhouse.domain.Address;
import com.lukasz.auctionhouse.domain.Address_;
import com.lukasz.auctionhouse.domain.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class CustomAddressValidator implements Validator {

    private static final String zipCodeRegex = "^[0-9]{2}-[0-9]{3}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return Address.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, Address_.FIRST_NAME, "Empty.firstName.field");
        ValidationUtils.rejectIfEmpty(errors, Address_.LAST_NAME, "Empty.lastName.field");
        ValidationUtils.rejectIfEmpty(errors, Address_.STREET, "Empty.street.field");
        ValidationUtils.rejectIfEmpty(errors, Address_.ZIP_CODE, "Empty.zipCode.field");
        ValidationUtils.rejectIfEmpty(errors, Address_.CITY, "Empty.city.field");
        ValidationUtils.rejectIfEmpty(errors, Address_.COUNTRY, "Empty.country.field");

        Address address = (Address) target;

        if(!address.getZipCode().matches(zipCodeRegex)){
            errors.rejectValue("zipCode", "Invalid.ZipCode", "Zip Code is incorrect.");
        }
    }

}
