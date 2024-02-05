package com.lukasz.auctionhouse.controllers.dto;

import com.lukasz.auctionhouse.domain.ItemStatus;
import com.lukasz.auctionhouse.domain.User;

import java.util.Date;
import java.util.Set;

public record ItemResponse(Long id,
                           String name,
                           Float startPrice,
                           Float buyItNowPrice,
                           String description,
                           Date expirationDate,
                           String status,
                           String boughtBy,
                           String listedBy,
                           String imagePath,
                           String itemCategory,
                           Set<String> itemProducers) {
}
