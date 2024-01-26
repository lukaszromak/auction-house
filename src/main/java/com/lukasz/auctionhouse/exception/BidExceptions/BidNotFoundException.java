package com.lukasz.auctionhouse.exception.BidExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BidNotFoundException extends RuntimeException {

    public BidNotFoundException(String message){
        super(message);
    }

}
