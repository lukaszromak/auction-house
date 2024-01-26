package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.BidHistoric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidHistoricRepository extends JpaRepository<BidHistoric, Long> {
}
