package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.controllers.dto.ItemResponse;
import com.lukasz.auctionhouse.controllers.mappers.ItemMapper;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.Item.ItemNotFoundException;
import com.lukasz.auctionhouse.exception.UserNotFoundException;
import com.lukasz.auctionhouse.service.ItemService;
import com.lukasz.auctionhouse.service.StorageService;
import com.lukasz.auctionhouse.service.UserService;
import com.lukasz.auctionhouse.validators.CustomItemValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import com.lukasz.auctionhouse.domain.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;
    private UserService userService;
    private StorageService storageService;
    private CustomItemValidator customItemValidator;
    private ItemMapper itemMapper;
    private static final Integer pageSize = 20;
    @Autowired
    public ItemController(ItemService itemService,
                          UserService userService,
                          StorageService storageService,
                          CustomItemValidator customItemValidator,
                          ItemMapper itemMapper){
        this.itemService = itemService;
        this.userService = userService;
        this.storageService = storageService;
        this.customItemValidator = customItemValidator;
        this.itemMapper = itemMapper;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(customItemValidator);
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemResponse createItem(@Valid @RequestBody Item item){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> listingUser = userService.getByUsername(username);
        if(listingUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with name %s not found.", username));
        }

        Item savedItem = itemService.saveItem(item, listingUser.get());
        item.setId(savedItem.getId());

        return itemMapper.toResponse(item);
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{itemId}/uploadImage")
    public ResponseEntity uploadItemImage(@PathVariable Long itemId,
                                          @RequestParam("imageFile") MultipartFile multipartFile) {
        Optional<Item> itemOptional = itemService.findItem(itemId);

        if(itemOptional.isEmpty()){
            throw new ItemNotFoundException(String.format("Item with id %d not found", itemId));
        }

        Item item = itemOptional.get();
        item.setImagePath(storageService.store(item, multipartFile));
        itemService.updateItem(item);

        return ResponseEntity.ok(String.format("Successfully uploaded image for item with id %d", itemId));
    }

    @GetMapping
    public Page<ItemResponse> getItems(
                            @RequestParam(name = "namePhrase", required = false) Optional<String> namePhrase,
                            @RequestParam(name = "descriptionPhrase", required = false) Optional<String> descriptionPhrase,
                            @RequestParam(name = "minPrice", required = false) Optional<Float> minPrice,
                            @RequestParam(name = "maxPrice", required = false) Optional<Float> maxPrice,
                            @RequestParam(name = "producerNames", required = false) Optional<String[]> producerNames,
                            @RequestParam(name = "categoryPhrase", required = false) Optional<String> categoryPhrase,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "dateMin", required = false) Optional<Date> dateMin,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "dateMax", required = false) Optional<Date> dateMax,
                            @RequestParam(name = "isBought", required = false) Optional<String> statusName,
                            @RequestParam(name = "isExpired", required = false) Optional<Boolean> expired,
                            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page){

        Page<Item> items = itemService.getAllItems(namePhrase, descriptionPhrase, minPrice, maxPrice, producerNames, categoryPhrase, dateMin, dateMax, statusName, expired, page, pageSize);

        return items.map(itemMapper::toResponse);
    }

    @GetMapping("/{itemId}")
    public ItemResponse getItem(@PathVariable Long itemId){
        Optional<Item> item = itemService.findItem(itemId);

        if(item.isEmpty()){
            throw new ItemNotFoundException(String.format("Item with id %d not found.", itemId));
        }

        return itemMapper.toResponse(item.get());
    }
    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}/buy")
    public ResponseEntity buyItem(@PathVariable(name = "itemId") Long itemId) {
        itemService.buyItem(itemId);
        return ResponseEntity.ok("Successfully bought item");
    }

}
