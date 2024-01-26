package com.lukasz.auctionhouse.configuration;

import com.lukasz.auctionhouse.domain.*;
import com.lukasz.auctionhouse.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;


@Configuration
public class RepositoriesInitializer {

    ItemCategoryRepository itemCategoryRepository;
    ItemProducerRepository itemProducerRepository;
    ItemRepository itemRepository;
    BidRepository bidRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public RepositoriesInitializer(ItemCategoryRepository itemCategoryRepository,
                                   ItemProducerRepository itemProducerRepository,
                                   ItemRepository itemRepository,
                                   BidRepository bidRepository,
                                   UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder){
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemProducerRepository = itemProducerRepository;
        this.itemRepository = itemRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    List<ItemCategory> itemCategory(){
        ItemCategory ic1 = new ItemCategory("Samochod");
        ItemCategory ic2 = new ItemCategory("Komputer");
        ItemCategory ic3 = new ItemCategory("Ogrod");

        return Arrays.asList(ic1, ic2, ic3);
    }

    @Bean
    List<ItemProducer> producers() {
       ItemProducer ip1 = new ItemProducer("Toyota");
       ItemProducer ip2 = new ItemProducer("BMW");
       ItemProducer ip3 = new ItemProducer("Microsoft");

       return Arrays.asList(ip1, ip2, ip3);
    }

    @Bean
    List<Item> items(){
        Calendar calendar = Calendar.getInstance();

        //bin price, no auction, not bought
        Item i1 = new Item();
        i1.setName("test");
        calendar.add(Calendar.YEAR, 1);
        i1.setExpirationDate(calendar.getTime());
        i1.setBought(false);
        i1.setBuyItNowPrice(10F);
        i1.setDescription("Bardzo fajny przedmiot.");

        //auction, no bin price, not bought
        Item i2 = new Item();
        i2.setName("test");
        calendar.add(Calendar.YEAR, 1);
        i2.setExpirationDate(calendar.getTime());
        i2.setStartPrice(1F);
        i2.setBought(false);
        i2.setDescription("Really cool item.");

        //auction, and bin price, not bought
        Item i3 = new Item();
        i3.setName("test2");
        calendar.add(Calendar.YEAR, 1);
        i3.setExpirationDate(calendar.getTime());
        i3.setStartPrice(1F);
        i3.setBuyItNowPrice(10F);
        i3.setBought(false);
        i3.setDescription("Sehr cooler Artikel.");

        return Arrays.asList(i1, i2, i3);
    }

    @Bean
    List<Bid> bids(){
        Bid bid2 = new Bid(null, null, 1F, null, System.currentTimeMillis());
        Bid bid3 = new Bid(null, null, 1F, null, System.currentTimeMillis());

        return Arrays.asList(bid2, bid3);
    }

    @Bean
    public List<Role> roles() {
        List<Role> roleList = new ArrayList<>();

        // Add sample roles for initialization
        roleList.add(new Role("ROLE_USER"));
        roleList.add(new Role("ROLE_ADMIN"));

        return roleList;
    }

    @Bean
    public List<User> users() {
        List<User> userList = new ArrayList<>();

        // Add sample users for initialization
        //
        userList.add(new User("luki", "lukasz.romak@gmail.com", passwordEncoder.encode("1234")));
        //ZHplam46MTIzNDIzNDU=
        userList.add(new User("dzejn", "jane@example.com", passwordEncoder.encode("12342345")));
        userList.add(new User("bob2341", "bob@example.com", passwordEncoder.encode("12342345")));

        return userList;
    }

    @Bean
    InitializingBean init(List<ItemCategory> itemCategories, List<ItemProducer> producers, List<Item> items, List<Bid> bids, List<User> users, List<Role> roles){
        return () -> {
            if(itemCategoryRepository.findAll().isEmpty()){
                itemCategoryRepository.saveAll(itemCategories);
            }
            if(itemProducerRepository.findAll().isEmpty()){
                itemProducerRepository.saveAll(producers);
            }
            if(itemRepository.findAll().isEmpty()){
                items.get(0).setItemCategory(itemCategories.get(0));
                items.get(1).setItemCategory(itemCategories.get(1));
                items.get(2).setItemCategory(itemCategories.get(2));
                items.get(0).setItemProducers(new HashSet<>(producers.subList(0, 2)));
                items.get(1).setItemProducers(new HashSet<>(producers.subList(1, 2)));
                items.get(2).setItemProducers(new HashSet<>(producers.subList(2, 3)));
                List<Item> savedItems = itemRepository.saveAll(items);
                System.out.println(savedItems);
                // ignoring first item, since it has no auction
                for(int i = 1; i < savedItems.size(); i++){
                    bids.get(i - 1).setItem(savedItems.get(i));
                }
                bidRepository.saveAll(bids);
            }
            if(roleRepository.findAll().isEmpty()){
                roleRepository.saveAll(roles);
            }
            if(userRepository.findAll().isEmpty()){
                users.get(0).setRoles(new HashSet<>(Arrays.asList(roles.get(0), roles.get(1))));
                users.get(1).setRoles(new HashSet<>(Arrays.asList(roles.get(0))));
                users.get(2).setRoles(new HashSet<>(Arrays.asList(roles.get(0))));
                userRepository.saveAll(users);
            }
        };
    }

}
