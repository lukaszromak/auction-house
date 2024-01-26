package com.lukasz.auctionhouse.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.lukasz.auctionhouse.domain.Item;

public class CustomItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Item.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "buyItNowPrice", "Empty.buyItNow.field");
        ValidationUtils.rejectIfEmpty(errors, "currentPrice", "Empty.currentPrice.field");
        Item item = (Item) target;
        if(item.getStartPrice() >= item.getBuyItNowPrice()){
            errors.rejectValue("buyItNowPrice","Item.Prices.Equal", "Auction start price cannot be higher or equal than buy it now price");
        }
    }
}
