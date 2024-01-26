package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b WHERE b.item.id = ?1")
    public Optional<Bid> findByItemId(Long itemId);
}
