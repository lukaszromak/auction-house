package com.lukasz.auctionhouse.exception.Item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemNotPricedException extends RuntimeException {

    public ItemNotPricedException(String message){
        super(message);
    }

}
