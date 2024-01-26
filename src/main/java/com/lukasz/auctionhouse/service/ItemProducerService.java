package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Item;
import com.lukasz.auctionhouse.domain.ItemProducer;
import com.lukasz.auctionhouse.repositories.ItemProducerRepository;
import com.lukasz.auctionhouse.repositories.ItemRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemProducerService {

    private ItemRepository itemRepository;
    private ItemProducerRepository itemProducerRepository;

    @Autowired
    public ItemProducerService(ItemRepository itemRepository, ItemProducerRepository itemProducerService){
        this.itemRepository = itemRepository;
        this.itemProducerRepository = itemProducerService;
    }

    public List<ItemProducer> getAllProducers(){
        return this.itemProducerRepository.findAll();
    }

    public List<ItemProducer> getProducers(List<Long> ids) {
        return itemProducerRepository.findAllById(ids);
    }

    public ItemProducer getProducer(Long producerId) {
        Optional<ItemProducer> itemProducer = itemProducerRepository.findById(producerId);

        if(itemProducer.isEmpty()) {
            throw new EntityNotFoundException(String.format("Item producer with id %d not found", producerId));
        }

        return itemProducer.get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemProducer saveProducer(ItemProducer itemProducer) {
        Optional<ItemProducer> itemProducerOptional = itemProducerRepository.findOneByName(itemProducer.getName());

        if(itemProducerOptional.isPresent()){
            throw new EntityExistsException(String.format("Category with '%s' already exists", itemProducer.getName()));
        }

        return itemProducerRepository.save(itemProducer);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemProducer deleteProducer(Long producerId){
        ItemProducer itemProducer = getProducer(producerId);

        List<Item> items = itemRepository.findAllByProducerId(itemProducer.getId());
        for(Item item: items){
            item.setItemProducers(item.getItemProducers()
                    .stream()
                    .filter((producer) -> !producer.equals(itemProducer))
                    .collect(Collectors.toSet()));
        }
        itemRepository.saveAll(items);

        itemProducerRepository.delete(itemProducer);

        return itemProducer;
    }

}
