package com.lukasz.auctionhouse.controllers;


import com.lukasz.auctionhouse.domain.Address;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.service.UserService;
import com.lukasz.auctionhouse.validators.CustomAddressValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private CustomAddressValidator customAddressValidator;
    @Autowired
    public UserController(UserService userService, CustomAddressValidator customAddressValidator){
        this.userService = userService;
        this.customAddressValidator = customAddressValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(customAddressValidator);
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @GetMapping("/{id}/address")
    public Address getAddress(@PathVariable Long id){
        return userService.findAddress(id);
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @PutMapping("/{id}/address")
    public ResponseEntity updateAddress(@PathVariable Long id, @Valid @RequestBody Address address){
        userService.updateAddress(id, address);
        return ResponseEntity.ok("User updated.");
    }


}
