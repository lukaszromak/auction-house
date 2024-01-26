package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.controllers.utils.BasicAuthUtils;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.Item.ItemNotFoundException;
import com.lukasz.auctionhouse.exception.UserNotFoundException;
import com.lukasz.auctionhouse.service.ItemService;
import com.lukasz.auctionhouse.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import com.lukasz.auctionhouse.domain.Item;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;
    private UserService userService;
    private BasicAuthUtils basicAuthUtils;
    @Autowired
    public ItemController(ItemService itemService, UserService userService, BasicAuthUtils basicAuthUtils){
        this.itemService = itemService;
        this.userService = userService;
        this.basicAuthUtils = basicAuthUtils;
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Item createItem(@RequestHeader(name = "Authorization") String authData, @Valid @RequestBody Item item){
        String username = basicAuthUtils.getUsernameFromAuthData(authData);
        Optional<User> listingUser = userService.getByUsername(username);
        if(listingUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with name %s not found.", username));
        }

        Item savedItem = itemService.saveItem(item, listingUser.get());
        // TODO: PRICES VALIDATION

        return item;
    }

    @GetMapping
    public List<Item> getItems(
                            @RequestParam(name = "namePhrase", required = false) Optional<String> namePhrase,
                            @RequestParam(name = "descriptionPhrase", required = false) Optional<String> descriptionPhrase,
                            @RequestParam(name = "minPrice", required = false) Optional<Float> minPrice,
                            @RequestParam(name = "maxPrice", required = false) Optional<Float> maxPrice,
                            @RequestParam(name = "producerNames", required = false) Optional<String[]> producerNames,
                            @RequestParam(name = "categoryPhrase", required = false) Optional<String> categoryPhrase,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "dateMin", required = false) Optional<Date> dateMin,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "dateMax", required = false) Optional<Date> dateMax){

        List<Item> items = itemService.getAllItems(namePhrase, descriptionPhrase, minPrice, maxPrice, producerNames, categoryPhrase, dateMin, dateMax);

        return items;
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable Long id){
        Optional<Item> item = itemService.findItem(id);

        if(item.isEmpty()){
            throw new ItemNotFoundException(String.format("Item with id %d not found.", id));
        }

        return item.get();
    }

    @PostMapping("/buy")
    public Item buyItem(@RequestHeader("Authorization") String authData, @RequestParam(name = "itemId") Long itemId){
        return itemService.buyItem(itemId, basicAuthUtils.getUsernameFromAuthData(authData));
    }

}
