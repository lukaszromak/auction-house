package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.domain.ItemProducer;
import com.lukasz.auctionhouse.service.ItemProducerService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itemProducers")
@Validated
public class ItemProducerController {

    private ItemProducerService itemProducerService;

    @Autowired
    public ItemProducerController(ItemProducerService itemProducerService){
        this.itemProducerService = itemProducerService;
    }

    @GetMapping
    public List<ItemProducer> getAllProducers() {
        return itemProducerService.getAllProducers();
    }

    @PostMapping
    public ItemProducer createItemProducer(@RequestParam @Size(min = 1, max = 30) String producerName) {
        return itemProducerService.saveProducer(new ItemProducer(producerName));
    }

    @DeleteMapping("/{producerId}")
    public ItemProducer deleteItemProducer(@PathVariable Long producerId){
        return itemProducerService.deleteProducer(producerId);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public String entityExists(Exception exception){
        return exception.getMessage();
    }
}
