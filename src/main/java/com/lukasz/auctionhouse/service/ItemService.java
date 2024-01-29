package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Bid;
import com.lukasz.auctionhouse.domain.Item;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.Item.ItemBoughtException;
import com.lukasz.auctionhouse.exception.Item.ItemNotFoundException;
import com.lukasz.auctionhouse.exception.Item.ItemNotPricedException;
import com.lukasz.auctionhouse.exception.UserNotFoundException;
import com.lukasz.auctionhouse.repositories.ItemRepository;
import com.lukasz.auctionhouse.repositories.ItemSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private BidService bidService;
    private UserService userService;
    private ItemRepository itemRepository;
    private ItemCategoryService itemCategoryService;
    private ItemProducerService itemProducerService;

    @Autowired
    public ItemService(BidService bidService, UserService userService, ItemRepository itemRepository, ItemCategoryService itemCategoryService, ItemProducerService itemProducerService){
        this.bidService = bidService;
        this.userService = userService;
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
        item.setItemCategory(itemCategoryService.getCategory(item.getItemCategory().getId()));
        item.setItemProducers(new HashSet<>(itemProducerService.getProducers(item
                .getItemProducers()
                .stream()
                .map(itemProducer -> itemProducer.getId())
                .toList())));
        item.setListedBy(listingUser);

        Item savedItem = itemRepository.save(item);

        if(savedItem.getStartPrice() != null){
            Bid bid = new Bid(null, savedItem, savedItem.getStartPrice(), null, System.currentTimeMillis());
            bidService.saveBid(bid);
        }

        return savedItem;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void updateItem(Item item){
        itemRepository.save(item);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Item buyItem(Long itemId, String username){
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

        if(item.isBought()){
            throw new ItemBoughtException(String.format("Item with id %d has been already bought.", itemId));
        }
        return item;
    }

    public Optional<Item> findItem(Long itemId){
        return itemRepository.findById(itemId);
    }

    public List<Item> getAllItems(Optional<String> namePhrase,
                                Optional<String> descriptionPhrase,
                                Optional<Float> minPrice,
                                Optional<Float> maxPrice,
                                Optional<String[]> producerNames,
                                Optional<String> categoryPhrase,
                                Optional<Date> dateMin,
                                Optional<Date> dateMax){
        List<Item> items;
        List<Specification<Item>> specifications = new ArrayList<>();

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

        if(specifications.isEmpty()){
            items = itemRepository.findAll();
        } else {
            items = itemRepository.findAll(Specification.allOf(specifications));
        }

        return items;
    }

}
