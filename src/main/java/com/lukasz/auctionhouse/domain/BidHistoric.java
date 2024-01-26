package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class BidHistoric {

    public BidHistoric(){}

    public BidHistoric(Long id, Item item, Float currentPrice, User user, Long timestamp){
        this.id = id;
        this.item = item;
        this.currentPrice = currentPrice;
        this.user = user;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_historic_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private float currentPrice;

    @NotNull
    private Long timestamp;
}
