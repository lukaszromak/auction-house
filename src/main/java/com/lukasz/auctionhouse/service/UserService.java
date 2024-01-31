package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Address;
import com.lukasz.auctionhouse.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> validUserNameAndPassword(String username, String password);
    Optional<User> getByUsername(String username);
    Optional<User> getById(Long id);
    Boolean hasUserWithUsername(String username);
    Boolean hasUserWithEmail(String email);
    User createUser(User user);
    void updateAddress(Long id, Address address);
    Address findAddress(Long id);
}
