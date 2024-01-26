package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.controllers.dto.BidRequest;
import com.lukasz.auctionhouse.controllers.dto.BidResponse;
import com.lukasz.auctionhouse.controllers.utils.BasicAuthUtils;
import com.lukasz.auctionhouse.domain.Bid;
import com.lukasz.auctionhouse.exception.BidExceptions.BidNotFoundException;
import com.lukasz.auctionhouse.service.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private BidService bidService;
    private BasicAuthUtils basicAuthUtils;

    @Autowired
    public BidController(BidService bidService, BasicAuthUtils basicAuthUtils){
        this.bidService = bidService;
        this.basicAuthUtils = basicAuthUtils;

    }

    @GetMapping("/{itemId}")
    private BidResponse getBid(@PathVariable Long itemId){
        Optional<Bid> bidOptional = bidService.getByItemId(itemId);

        if(bidOptional.isEmpty()){
            throw new BidNotFoundException(String.format("Bid with item id %d not found", itemId));
        }

        Bid bid = bidOptional.get();

        return new BidResponse(bid.getId(), bid.getUser(), bid.getCurrentPrice(), bid.getTimestamp());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/placeBid")
    private BidResponse placeBid(@RequestHeader("Authorization") String authData, @Valid @RequestBody BidRequest bidRequest){
        Bid bid = bidService.placeBid(bidRequest, basicAuthUtils.getUsernameFromAuthData(authData));

        return new BidResponse(bid.getId(), bid.getUser(), bid.getCurrentPrice(), bid.getTimestamp());
    }

}
