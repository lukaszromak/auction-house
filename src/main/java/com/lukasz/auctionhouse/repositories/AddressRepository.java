package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.id=?1")
    Optional<Address> findByUserId(Long userId);

}
