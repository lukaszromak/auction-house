package com.lukasz.auctionhouse.exception.Item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemExpiredException extends RuntimeException {

    public ItemExpiredException(String message){
        super(message);
    }

}
