package com.eviive.personalapi.core.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.eviive.personalapi.core.properties.AzureStoragePropertiesConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {

    @Bean
    public BlobServiceClient blobServiceClient(
        final AzureStoragePropertiesConfig azureStoragePropertiesConfig
    ) {
        return new BlobServiceClientBuilder()
            .connectionString(azureStoragePropertiesConfig.connectionString())
            .buildClient();
    }

}
