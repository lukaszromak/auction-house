package com.lukasz.auctionhouse.configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "storage.service", havingValue = "azure")
public class AzureStorageConfig {
    @Value("${azure.storage.connection.string}")
    private String connectionString;
    @Value("${azure.storage.container.name}")
    private String containerName;
    @Bean
    public BlobServiceClient blobServiceClient(){
        return new BlobServiceClientBuilder()
                .endpoint("https://lrauctionhouse.blob.core.windows.net/")
                .connectionString(connectionString)
                .buildClient();
    }
    @Bean
    public BlobContainerClient blobContainerClient(BlobServiceClient blobServiceClient){
        return blobServiceClient.getBlobContainerClient(containerName);
    }

}
