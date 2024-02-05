package com.lukasz.auctionhouse.controllers.mappers;

import com.lukasz.auctionhouse.controllers.dto.ItemResponse;
import com.lukasz.auctionhouse.domain.Item;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ItemMapper {

    public ItemResponse toResponse(Item item){
        String boughtByUserName = null;
        String listedByUserName = null;
        String itemCategoryName = null;

        if(item.getBoughtBy() != null){
            boughtByUserName = item.getBoughtBy().getUsername();
        }

        if(item.getListedBy() != null){
            listedByUserName = item.getListedBy().getUsername();
        }

        if(item.getItemCategory() != null){
            itemCategoryName = item.getItemCategory().getName();
        }

        ItemResponse itemResponse = new ItemResponse(
                item.getId(),
                item.getName(),
                item.getStartPrice(),
                item.getBuyItNowPrice(),
                item.getDescription(),
                item.getExpirationDate(),
                item.getStatus().getName(),
                boughtByUserName,
                listedByUserName,
                item.getImagePath(),
                itemCategoryName,
                item.getItemProducers()
                        .stream()
                        .map(itemProducer -> itemProducer.getName())
                        .collect(Collectors.toSet())
        );

        return itemResponse;
    }

}
