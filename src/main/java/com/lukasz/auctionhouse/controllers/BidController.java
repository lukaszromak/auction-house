package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.controllers.dto.BidRequest;
import com.lukasz.auctionhouse.controllers.dto.BidResponse;
import com.lukasz.auctionhouse.controllers.mappers.ItemMapper;
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
    private ItemMapper itemMapper;

    @Autowired
    public BidController(BidService bidService, ItemMapper itemMapper){
        this.bidService = bidService;
        this.itemMapper = itemMapper;
    }

    @GetMapping("/{itemId}")
    private BidResponse getBid(@PathVariable Long itemId){
        Optional<Bid> bidOptional = bidService.getByItemId(itemId);

        if(bidOptional.isEmpty()){
            throw new BidNotFoundException(String.format("Bid with item id %d not found", itemId));
        }

        Bid bid = bidOptional.get();

        return new BidResponse(bid.getId(), itemMapper.toResponse(bid.getItem()), bid.getCurrentPrice(), bid.getUser().getUsername(), bid.getTimestamp());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/placeBid")
    private Bid placeBid(@RequestHeader("Authorization") String authData, @Valid @RequestBody BidRequest bidRequest){
        return bidService.placeBid(bidRequest);
    }

}
