package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Item;
import com.lukasz.auctionhouse.exception.Item.ItemNotFoundException;
import com.lukasz.auctionhouse.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocalStorageService implements StorageService {

    private final ItemRepository itemRepository;
    @Value("${upload.location}")
    private String basePath;

    @Autowired
    public LocalStorageService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public String store(Item item, MultipartFile multipartFile) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String listedByUsername = item.getListedBy().getUsername();

        if(listedByUsername == null || !Objects.equals(username, item.getListedBy().getUsername())){
            throw new RuntimeException(String.format("That item does not belong to user %s", username));
        }

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String filename = String.format("%d.%s", item.getId(), extension);
        File file = new File(basePath + filename);

        try {
            multipartFile.transferTo(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return filename;
    }
}
