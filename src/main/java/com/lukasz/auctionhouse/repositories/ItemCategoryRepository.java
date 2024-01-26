package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    Optional<ItemCategory> findOneByName(String name);
}
