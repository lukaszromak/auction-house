package com.lukasz.auctionhouse.controllers.dto;

public record BidResponse(Long id,
                          ItemResponse item,
                          Float currentPrice,
                          String username,
                          Long timestamp
                          ) {
}
