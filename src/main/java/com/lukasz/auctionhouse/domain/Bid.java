package com.lukasz.auctionhouse.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(BidListener.class)
public class Bid {

    public Bid(Long id, Item item, Float currentPrice, User user, Long timestamp){
        this.id = id;
        this.item = item;
        this.currentPrice = currentPrice;
        this.user = user;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Float currentPrice;

    @NotNull
    private Long timestamp;
}
