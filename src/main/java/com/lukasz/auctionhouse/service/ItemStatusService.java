package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.ItemStatus;
import com.lukasz.auctionhouse.repositories.ItemStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemStatusService {

    private ItemStatusRepository itemStatusRepository;

    @Autowired
    public ItemStatusService(ItemStatusRepository itemStatusRepository){
        this.itemStatusRepository = itemStatusRepository;
    }

    public ItemStatus findByName(String name){
        Optional<ItemStatus> itemStatus = itemStatusRepository.findByName(name);

        if(itemStatus.isEmpty()){
            throw new EntityNotFoundException(String.format("Status with name %s not found.", name));
        }

        return itemStatus.get();
    }

}
