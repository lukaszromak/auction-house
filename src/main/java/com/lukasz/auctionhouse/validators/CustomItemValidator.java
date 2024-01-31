package com.lukasz.auctionhouse.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.lukasz.auctionhouse.domain.Item;

@Component
public class CustomItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        if(item.getStartPrice() != null && item.getBuyItNowPrice() != null && item.getStartPrice() >= item.getBuyItNowPrice()){
            errors.rejectValue("buyItNowPrice","Item.Prices.Equal", "Auction start price cannot be higher or equal than buy it now price.");
        }
        if(item.getStartPrice() == null && item.getBuyItNowPrice() == null){
            errors.rejectValue("getStartPrice", "No.Price.Set", "Either start price or buy it now price has to be set.");
        }
    }
}
