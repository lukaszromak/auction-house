package com.lukasz.auctionhouse.exception.Item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemBoughtException extends RuntimeException {

    public ItemBoughtException(String message){
        super(message);
    }

}
