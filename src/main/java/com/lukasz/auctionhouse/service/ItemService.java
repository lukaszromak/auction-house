package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Bid;
import com.lukasz.auctionhouse.domain.Item;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.Item.*;
import com.lukasz.auctionhouse.exception.UserNotFoundException;
import com.lukasz.auctionhouse.repositories.ItemRepository;
import com.lukasz.auctionhouse.repositories.ItemSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private BidService bidService;
    private UserService userService;
    private ItemStatusService itemStatusService;
    private ItemRepository itemRepository;
    private ItemCategoryService itemCategoryService;
    private ItemProducerService itemProducerService;

    @Autowired
    public ItemService(BidService bidService,
                       UserService userService,
                       ItemStatusService itemStatusService,
                       ItemRepository itemRepository,
                       ItemCategoryService itemCategoryService,
                       ItemProducerService itemProducerService){
        this.bidService = bidService;
        this.userService = userService;
        this.itemStatusService = itemStatusService;
        this.itemRepository = itemRepository;
        this.itemCategoryService = itemCategoryService;
        this.itemProducerService = itemProducerService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Item saveItem(Item item, User listingUser){
        if(item.getStartPrice() == null && item.getBuyItNowPrice() == null) {
            throw new ItemNotPricedException("Item needs to have either buy it now price or starting price.");
        }

        item.setId(null);
        if(item.getItemCategory() != null) {
            item.setItemCategory(itemCategoryService.getCategory(item.getItemCategory().getId()));
        }
        if(item.getItemProducers() != null) {
            item.setItemProducers(new HashSet<>(itemProducerService.getProducers(item
                    .getItemProducers()
                    .stream()
                    .map(itemProducer -> itemProducer.getId())
                    .toList())));
        }
        item.setListedBy(listingUser);
        item.setStatus(itemStatusService.findByName("NOT_BOUGHT"));

        Item savedItem = itemRepository.save(item);

        if(savedItem.getStartPrice() != null){
            Bid bid = new Bid(null, savedItem, savedItem.getStartPrice(), listingUser, System.currentTimeMillis());
            bidService.saveBid(bid);
        }

        return savedItem;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void updateItem(Item item){
        itemRepository.save(item);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void buyItem(Long itemId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Optional<User> userOptional = userService.getByUsername(username);

        if(itemOptional.isEmpty()){
            throw new ItemNotFoundException(String.format("Item with id %d not found.", itemId));
        }

        if(userOptional.isEmpty()){
            throw new UserNotFoundException(String.format("User with name %s not found.", username));
        }

        Item item = itemOptional.get();
        User user = userOptional.get();

        if(item.getListedBy().equals(user)) {
            throw new OwnItemBuyException("You cannot buy item you own.");
        }

        if(!item.getStatus().getName().equals("NOT_BOUGHT")){
            throw new ItemBoughtException(String.format("Item with id %d has been already bought.", itemId));
        }

        if(item.getBuyItNowPrice() == null){
            throw new ItemNotPricedException("That item has no buy it now option");
        }

        if(System.currentTimeMillis() >= item.getExpirationDate().getTime()){
            throw new ItemExpiredException(String.format("Time to buy item id %d has expired", itemId));
        }

        item.setStatus(itemStatusService.findByName("BOUGHT_BIN"));
        item.setBoughtBy(user);
        itemRepository.save(item);
    }

    public Optional<Item> findItem(Long itemId){
        return itemRepository.findById(itemId);
    }

    public Page<Item> getAllItems(Optional<String> namePhrase,
                                  Optional<String> descriptionPhrase,
                                  Optional<Float> minPrice,
                                  Optional<Float> maxPrice,
                                  Optional<String[]> producerNames,
                                  Optional<String> categoryPhrase,
                                  Optional<Date> dateMin,
                                  Optional<Date> dateMax,
                                  Optional<String> statusName,
                                  Optional<Boolean> expired,
                                  Integer page,
                                  Integer pageSize){
        Page<Item> items;
        List<Specification<Item>> specifications = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize);

        if(namePhrase.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByPhrase(namePhrase.get())));
        }
        if(descriptionPhrase.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByDescriptionPhrase(descriptionPhrase.get())));
        }
        if(minPrice.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByPriceGreaterThan(minPrice.get())));
        }
        if(maxPrice.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByPriceLessThan(maxPrice.get())));
        }
        if(producerNames.isPresent()){
            for(String producerName: producerNames.get()){
                specifications.add(Specification.where(ItemSpecifications.findByProducerName(producerName)));
            }
        }
        if(categoryPhrase.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByCategoryNamePhrase(categoryPhrase.get())));
        }
        if(dateMin.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByDateAfter(dateMin.get())));
        }
        if(dateMax.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByDateBefore(dateMax.get())));
        }
        if(statusName.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByIsBought(statusName.get())));
        }
        if(expired.isPresent()){
            specifications.add(Specification.where(ItemSpecifications.findByExpired(expired.get())));
        }


        if(specifications.isEmpty()){
            items = itemRepository.findAll(pageable);
        } else {
            items = itemRepository.findAll(Specification.allOf(specifications), pageable);
        }

        return items;
    }

}
