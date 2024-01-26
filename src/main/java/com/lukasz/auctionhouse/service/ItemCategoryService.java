package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Item;
import com.lukasz.auctionhouse.domain.ItemCategory;
import com.lukasz.auctionhouse.repositories.ItemCategoryRepository;
import com.lukasz.auctionhouse.repositories.ItemRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCategoryService {

    private ItemRepository itemRepository;
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    public ItemCategoryService(ItemRepository itemRepository, ItemCategoryRepository itemCategoryRepository) {
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
    }

    public List<ItemCategory> getAllCategories(){
        return itemCategoryRepository.findAll();
    }

    public ItemCategory getCategory(Long categoryId) {
        Optional<ItemCategory> itemCategory = itemCategoryRepository.findById(categoryId);

        if(itemCategory.isEmpty()) {
            throw new EntityNotFoundException(String.format("Item category with id %d not found", categoryId));
        }

        return itemCategory.get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemCategory saveCategory(ItemCategory itemCategory) {
        Optional<ItemCategory> itemCategoryOptional = itemCategoryRepository.findOneByName(itemCategory.getName());

        if(itemCategoryOptional.isPresent()){
            throw new EntityExistsException(String.format("Category with '%s' already exists", itemCategory.getName()));
        }

        return itemCategoryRepository.save(itemCategory);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemCategory deleteCategory(Long categoryId){
        ItemCategory itemCategory = getCategory(categoryId);

        List<Item> items = itemRepository.findAllByCategoryId(itemCategory.getId());
        for(Item item: items){
            item.setItemCategory(null);
        }
        itemRepository.saveAll(items);

        itemCategoryRepository.delete(itemCategory);

        return itemCategory;
    }
}
