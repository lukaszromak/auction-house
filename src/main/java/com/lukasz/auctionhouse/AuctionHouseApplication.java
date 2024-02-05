package com.lukasz.auctionhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class AuctionHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionHouseApplication.class, args);
    }

}
