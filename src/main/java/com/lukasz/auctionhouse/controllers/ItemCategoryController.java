package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.domain.ItemCategory;
import com.lukasz.auctionhouse.service.ItemCategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itemCategories")
@Validated
public class ItemCategoryController {

    private ItemCategoryService itemCategoryService;

    @Autowired
    public ItemCategoryController(ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
    }

    @GetMapping
    public List<ItemCategory> getAllCategories() {
        return itemCategoryService.getAllCategories();
    }

    @PostMapping
    public ItemCategory createItemCategory(@RequestParam @Size(min = 1, max = 30) String categoryName) {
        return itemCategoryService.saveCategory(new ItemCategory(categoryName));
    }

    @DeleteMapping("/{categoryId}")
    public ItemCategory deleteItemCategory(@PathVariable Long categoryId){
        return itemCategoryService.deleteCategory(categoryId);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public String entityExists(Exception exception){
        return exception.getMessage();
    }
}
