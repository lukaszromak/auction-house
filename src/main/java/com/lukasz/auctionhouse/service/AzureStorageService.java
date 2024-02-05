package com.lukasz.auctionhouse.service;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.lukasz.auctionhouse.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "storage.service", havingValue = "azure")
public class AzureStorageService implements StorageService{
    private List<String> allowedExtensions = Arrays.asList(new String[]{"jpg", "png"});
    private BlobContainerClient blobContainerClient;

    @Autowired
    public AzureStorageService(BlobContainerClient blobContainerClient){
        this.blobContainerClient = blobContainerClient;
    }

    @Override
    public String store(Item item, MultipartFile multipartFile) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String listedByUsername = item.getListedBy().getUsername();

        if(listedByUsername == null || !Objects.equals(username, item.getListedBy().getUsername())){
            throw new RuntimeException(String.format("That item does not belong to user %s", username));
        }

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        if(!allowedExtensions.contains(extension)){
            throw new IllegalArgumentException("Illegal file extension.");
        }

        String filename = String.format("%d.%s", item.getId(), extension);
        BlobClient blobClient = blobContainerClient.getBlobClient(filename);

        try {
            blobClient.upload(multipartFile.getInputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filename;
    }

}
