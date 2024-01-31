package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;


public class ItemSpecifications {

    public static Specification<Item> findByPhrase(final String phrase){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(!phrase.isEmpty()){
                String phraseLike = "%" + phrase.toUpperCase() + "%";
                return criteriaBuilder.like(criteriaBuilder.upper(itemRoot.get(Item_.name)), phraseLike);
            }

            return null;
        };
    }

    public static Specification<Item> findByDescriptionPhrase(final String descriptionPhrase){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(!descriptionPhrase.isEmpty()){
                String phraseLike = "%" + descriptionPhrase.toUpperCase() + "%";
                return criteriaBuilder.like(criteriaBuilder.upper(itemRoot.get(Item_.description)), phraseLike);
            }

            return null;
        };
    }

    public static Specification<Item> findByPriceGreaterThan(final Float price){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(price != null){
                return criteriaBuilder.greaterThan(itemRoot.get(Item_.buyItNowPrice), price);
            }

            return null;
        };
    }

    public static Specification<Item> findByPriceLessThan(final Float price){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(price != null){
                return criteriaBuilder.lessThan(itemRoot.get(Item_.buyItNowPrice), price);
            }

            return null;
        };
    }

    public static Specification<Item> findByDateAfter(final Date date) {
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if (date != null) {
                return criteriaBuilder.greaterThan(itemRoot.get(Item_.expirationDate), date);

            }

            return null;
        };
    }

    public static Specification<Item> findByDateBefore(final Date date) {
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if (date != null) {
                return criteriaBuilder.lessThan(itemRoot.get(Item_.expirationDate), date);

            }

            return null;
        };
    }

    public static Specification<Item> findByProducerNamePhrase(final String phrase){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(!phrase.isEmpty()){
                String phraseLike = "%" + phrase.toUpperCase() + "%";
                Join<Item, ItemProducer> itemItemProducerJoin = itemRoot.join(Item_.ITEM_PRODUCERS);
                return criteriaBuilder.like(criteriaBuilder.upper(itemItemProducerJoin.get(ItemProducer_.name)), phraseLike);
            }

            return null;
        };
    }

    public static Specification<Item> findByProducerName(final String name){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(name != null){
                Join<Item, ItemProducer> itemItemProducerJoin = itemRoot.join(Item_.ITEM_PRODUCERS);
                return criteriaBuilder.equal(criteriaBuilder.upper(itemItemProducerJoin.get(ItemProducer_.name)), name);
            }

            return null;
        };
    }

    public static Specification<Item> findByCategoryNamePhrase(final String phrase){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(!phrase.isEmpty()){
                String phraseLike = "%" + phrase.toUpperCase() + "%";
                Join<Item, ItemCategory> itemItemCategoryJoin = itemRoot.join(Item_.ITEM_CATEGORY);
                return criteriaBuilder.like(criteriaBuilder.upper(itemItemCategoryJoin.get(ItemCategory_.name)), phraseLike);
            }

            return null;
        };
    }

    public static Specification<Item> findByIsBought(final String statusName){
        return (itemRoot, criteriaQuery, criteriaBuilder) -> {
            if(!statusName.isEmpty()){
                String phraseLike = "%" + statusName.toUpperCase() + "%";
                Join<Item, ItemCategory> itemItemStatusJoin = itemRoot.join(Item_.STATUS);
                return criteriaBuilder.equal(itemItemStatusJoin.get(ItemStatus_.NAME), statusName);
            }

            return null;
        };
    }
}
