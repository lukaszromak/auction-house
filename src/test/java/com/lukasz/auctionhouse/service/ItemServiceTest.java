package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.AuctionHouseApplication;
import com.lukasz.auctionhouse.domain.Bid;
import com.lukasz.auctionhouse.domain.Item;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.Item.ItemNotFoundException;
import com.lukasz.auctionhouse.exception.Item.ItemNotPricedException;
import com.lukasz.auctionhouse.exception.UserNotFoundException;
import com.lukasz.auctionhouse.repositories.ItemRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.lukasz.auctionhouse.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = AuctionHouseApplication.class)
public class ItemServiceTest {

    @InjectMocks
    public ItemService itemService;
    @Mock
    public BidService bidService;
    @Mock
    public UserService userService;
    @Mock
    public ItemStatusService itemStatusService;
    @Mock
    public ItemRepository itemRepository;
    @Mock
    public ItemCategoryService itemCategoryService;
    @Mock
    public ItemProducerService itemProducerService;
    private Item item;
    private Item savedItem;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeAll
    public void classSetUp() {
        Optional<User> userOptional = userRepository.findByUsername("testuser");

        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("Test user not created!");
        } else {
            user = userOptional.get();
        }
    }

    @BeforeEach
    public void setUp() {
        item = new Item();
        item.setStartPrice(1f);
        item.setBuyItNowPrice(10f);
        savedItem = new Item();
        savedItem.setId(1l);
        savedItem.setStartPrice(1f);
        savedItem.setBuyItNowPrice(10f);

        doReturn(savedItem).when(itemRepository).save(item);
    }

    @Test
    public void givenStartPriceNullAndBuyItNowPriceNullWhenSaveItemThenItemNotPricedException() {
        item.setStartPrice(null);
        item.setBuyItNowPrice(null);

        Exception e = assertThrows(ItemNotPricedException.class, () -> {
            itemService.saveItem(item, user);
        });
    }

    @Test
    public void givenStartPriceSetAndBuyItNowPriceNullWhenSaveItemThenItemSaved() {
        item.setBuyItNowPrice(null);

        itemService.saveItem(item, user);

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void givenStartPriceNullAndBuyItNowPriceSetWhenSaveItemThenItemSaved() {
        item.setStartPrice(null);

        itemService.saveItem(item, user);

        verify(itemRepository, times(1)).save(item);
    }


    @Test
    public void givenStartPriceSetAndBuyItNowPriceSetWhenSaveItemThenItemSaved() {
        itemService.saveItem(item, user);

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void givenStartPriceSetWhenSaveItemThenBidCreated() {
        itemService.saveItem(item, user);

        verify(bidService, times(1)).saveBid(any(Bid.class));
    }

    @Test
    public void whenSaveItemThenUserBoundToItem() {
        itemService.saveItem(item, user);

        assertEquals(item.getListedBy(), user);
    }
}
