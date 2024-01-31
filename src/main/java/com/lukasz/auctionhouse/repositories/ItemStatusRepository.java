package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {
    Optional<ItemStatus> findByName(String name);
}
