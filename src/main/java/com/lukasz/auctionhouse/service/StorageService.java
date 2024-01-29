package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Item;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(Item item, MultipartFile multipartFile);
}
