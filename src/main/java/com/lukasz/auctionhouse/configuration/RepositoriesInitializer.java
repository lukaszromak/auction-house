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
    ItemStatusRepository itemStatusRepository;
    ItemRepository itemRepository;
    PostRepository postRepository;
    BidRepository bidRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public RepositoriesInitializer(ItemCategoryRepository itemCategoryRepository,
                                   ItemProducerRepository itemProducerRepository,
                                   ItemStatusRepository itemStatusRepository,
                                   ItemRepository itemRepository,
                                   BidRepository bidRepository,
                                   UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder,
                                   PostRepository postRepository){
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemProducerRepository = itemProducerRepository;
        this.itemStatusRepository = itemStatusRepository;
        this.itemRepository = itemRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
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
        i1.setBuyItNowPrice(10F);
        i1.setDescription("Bardzo fajny przedmiot.");

        //auction, no bin price, not bought
        Item i2 = new Item();
        i2.setName("test");
        calendar.add(Calendar.YEAR, 1);
        i2.setExpirationDate(calendar.getTime());
        i2.setStartPrice(1F);
        i2.setDescription("Really cool item.");

        //auction, and bin price, not bought
        Item i3 = new Item();
        i3.setName("test2");
        calendar.add(Calendar.YEAR, 1);
        i3.setExpirationDate(calendar.getTime());
        i3.setStartPrice(1F);
        i3.setBuyItNowPrice(10F);
        i3.setDescription("Sehr cooler Artikel.");

        return Arrays.asList(i1, i2, i3);
    }

    @Bean
    List<ItemStatus> itemStatuses(){
        ItemStatus itemStatus = new ItemStatus();
        itemStatus.setName("NOT_BOUGHT");

        ItemStatus itemStatus1 = new ItemStatus();
        itemStatus1.setName("BOUGHT_AUCTION");

        ItemStatus itemStatus2 = new ItemStatus();
        itemStatus2.setName("BOUGHT_BIN");

        return Arrays.asList(itemStatus, itemStatus1, itemStatus2);
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
        roleList.add(new Role("ROLE_MODERATOR"));

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
        userList.add(new User("moderator", "moderator@gmail.com", passwordEncoder.encode("moderator")));

        return userList;
    }

    @Bean
    public List<Post> posts() {
        List<Post> postList = new ArrayList<>();

        Post p1 = new Post();
        Post p2 = new Post();
        Post p3 = new Post();


        p1.setTitle("Rare Art Extravaganza: Unveiling Masterpieces from Renowned Artists!");
        p1.setContent("Join us for an exclusive auction featuring a collection of rare and exquisite artworks from acclaimed artists around the globe. From breathtaking paintings to avant-garde sculptures, this event promises to be a feast for art enthusiasts. Mark your calendars and don't miss the opportunity to bid on these extraordinary masterpieces. Stay tuned for more details and a sneak peek into the world of artistic brilliance! \uD83C\uDFA8\uD83D\uDD28");
        p2.setTitle("Timeless Elegance: Vintage Jewelry Auction");
        p2.setContent("Indulge in the allure of vintage elegance at our upcoming jewelry auction! On We present a dazzling array of timeless pieces, including rare gemstones, heirloom-quality watches, and vintage accessories. Whether you're a collector or seeking a unique statement piece, this auction is your chance to acquire something truly special. Don't miss out on the chance to own a piece of history and add a touch of sophistication to your collection. \uD83D\uDC8D✨");
        p3.setTitle("Treasures of the Orient: Asian Antiques Auction");
        p3.setContent("Embark on a journey through the rich cultural heritage of the East at our upcoming Asian Antiques Auction! Scheduled for [Date], this event will showcase a curated selection of exquisite artifacts, ceramics, and textiles from various Asian dynasties. Discover the beauty and craftsmanship of centuries-old treasures and immerse yourself in the mystique of the Orient. Stay tuned for a glimpse into the captivating world of Asian antiques! \uD83C\uDFFA\uD83C\uDF0F");

        postList.add(p1);
        postList.add(p2);
        postList.add(p3);

        return postList;
    }

    @Bean
    InitializingBean init(List<ItemCategory> itemCategories,
                          List<ItemProducer> producers,
                          List<Item> items,
                          List<ItemStatus> itemStatuses,
                          List<Bid> bids,
                          List<User> users,
                          List<Role> roles,
                          List<Post> posts){
        return () -> {
            if(itemCategoryRepository.findAll().isEmpty()){
                itemCategoryRepository.saveAll(itemCategories);
            }
            if(itemProducerRepository.findAll().isEmpty()){
                itemProducerRepository.saveAll(producers);
            }
            if(itemStatusRepository.findAll().isEmpty()){
                itemStatusRepository.saveAll(itemStatuses);
            }
            if(itemRepository.findAll().isEmpty()){
                items.get(0).setItemCategory(itemCategories.get(0));
                items.get(1).setItemCategory(itemCategories.get(1));
                items.get(2).setItemCategory(itemCategories.get(2));
                items.get(0).setItemProducers(new HashSet<>(producers.subList(0, 2)));
                items.get(1).setItemProducers(new HashSet<>(producers.subList(1, 2)));
                items.get(2).setItemProducers(new HashSet<>(producers.subList(2, 3)));
                items.get(0).setStatus(itemStatuses.get(0));
                items.get(1).setStatus(itemStatuses.get(0));
                items.get(2).setStatus(itemStatuses.get(0));
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
                users.get(3).setRoles(new HashSet<>(Arrays.asList(roles.get(2))));
                userRepository.saveAll(users);
            }
            if(postRepository.findAll().isEmpty()){
                postRepository.saveAll(posts);
            }
        };
    }

}
