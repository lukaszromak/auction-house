package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.ItemProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemProducerRepository extends JpaRepository<ItemProducer, Long> {
    Optional<ItemProducer> findOneByName(String name);
}
