package com.lukasz.auctionhouse.exception.Item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OwnItemBuyException extends RuntimeException {

    public OwnItemBuyException(String message){
        super(message);
    }

}
