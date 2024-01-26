package com.lukasz.auctionhouse.controllers.dto;

import com.lukasz.auctionhouse.domain.Role;

import java.util.Set;

public record AuthResponse(Long id, String username, String email, Set<Role> roles) {
}