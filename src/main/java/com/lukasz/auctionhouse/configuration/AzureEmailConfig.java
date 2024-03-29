package com.lukasz.auctionhouse.configuration;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "email.service", havingValue = "true")
public class AzureEmailConfig {

    @Value("${AZURE_EMAIL_CONNECTION_STRING}")
    private String connectionString;

    @Bean
    public EmailClient emailClient(){
        return new EmailClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

}
