package com.lukasz.auctionhouse.domain;


import com.lukasz.auctionhouse.service.BidService;
import jakarta.persistence.PreUpdate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class BidListener {

    private final BidService bidService;

    public BidListener(@Lazy BidService bidService){
        this.bidService = bidService;
    }

    @PreUpdate
    public void preUpdate(Bid bid){
        BidHistoric bidHistoric = new BidHistoric(null, bid.getItem(), bid.getCurrentPrice(), bid.getUser(), bid.getTimestamp());
        bidService.saveBid(bidHistoric);
    }

}
