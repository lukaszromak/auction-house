package com.lukasz.auctionhouse.controllers.dto;

import com.lukasz.auctionhouse.domain.User;

public record BidResponse(Long id, User user, Float currentPrice, Long timestamp) {
}
