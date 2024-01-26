package com.lukasz.auctionhouse.exception.BidExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SelfOutbidException extends RuntimeException {

    public SelfOutbidException(String message){
        super(message);
    }

}
