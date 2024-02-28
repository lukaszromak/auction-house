package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.controllers.dto.BidRequest;
import com.lukasz.auctionhouse.domain.Bid;
import com.lukasz.auctionhouse.domain.BidHistoric;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.*;
import com.lukasz.auctionhouse.exception.BidExceptions.*;
import com.lukasz.auctionhouse.exception.Item.OwnItemBuyException;
import com.lukasz.auctionhouse.repositories.BidHistoricRepository;
import com.lukasz.auctionhouse.repositories.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BidService {

    private BidRepository bidRepository;
    private BidHistoricRepository bidHistoricRepository;
    private UserService userService;

    @Autowired
    public BidService(BidRepository bidRepository, BidHistoricRepository bidHistoricRepository, UserService userService){
        this.bidRepository = bidRepository;
        this.bidHistoricRepository = bidHistoricRepository;
        this.userService = userService;
    }

    public Bid saveBid(Bid bid){
        return bidRepository.save(bid);
    }

    public BidHistoric saveBid(BidHistoric bidHistoric){
        return bidHistoricRepository.save(bidHistoric);
    }

    public Optional<Bid> findBid(Long bidId){
        return bidRepository.findById(bidId);
    }

    public Optional<Bid> getByItemId(Long itemId){
        return bidRepository.findByItemId(itemId);
    }

    public Long getHistoricCount(){
        return bidHistoricRepository.count();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Bid placeBid(BidRequest bidRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getByUsername(username);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException(String.format("User not found."));
        }

        Optional<Bid> bidOptional = findBid(bidRequest.getBidId());

        if(bidOptional.isEmpty()){
            throw new BidNotFoundException(String.format("Bid with id %d not found.", bidRequest.getBidId()));
        }

        Bid bid = bidOptional.get();
        User user = userOptional.get();

        if(!bid.getItem().getStatus().getName().equals("NOT_BOUGHT")){
            throw new BidOnBoughtItem("You cannot place bid on bought item.");
        }

        if(bid.getItem().getExpirationDate().getTime() <= System.currentTimeMillis()){
            throw new AuctionEndedException("That auction has ended.");
        }

        if(bid.getItem().getListedBy().equals(user)) {
            throw new OwnItemBuyException("You cannot place bid on item you listed.");
        }

        if(bid.getUser() != null && bid.getUser().equals(user)){
            throw new SelfOutbidException("You cannot outbid yourself.");
        }

        if(bidRequest.getOffer() != null && bidRequest.getOffer() <= bid.getCurrentPrice()){
            throw new InvalidBidOfferException("Your offer is lower or equal to current price.");
        }

        Float buyItNowPrice = bid.getItem().getBuyItNowPrice();
        if(bidRequest.getOffer() != null && buyItNowPrice != null && bidRequest.getOffer() >= buyItNowPrice){
            throw new InvalidBidOfferException("Your offer cannot be higher or equal to buy it now price.");
        }

        bid.setUser(user);
        bid.setTimestamp(System.currentTimeMillis());
        bid.setCurrentPrice(bidRequest.getOffer());
        return bidRepository.save(bid);
    }
}
