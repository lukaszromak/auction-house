package com.lukasz.auctionhouse.controllers.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class BasicAuthUtils {

    private Base64.Decoder base64Decoder;

    public BasicAuthUtils() {
        this.base64Decoder = Base64.getDecoder();
    }

    public String getUsernameFromAuthData(String authData){
        String decodedAuthData = new String(base64Decoder.decode(authData.split(" ")[1]));
        String username = decodedAuthData.split(":")[0];

        return username;
    }

}
