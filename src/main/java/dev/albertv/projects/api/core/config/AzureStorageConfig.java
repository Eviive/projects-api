package dev.albertv.projects.api.core.config;

import com.azure.spring.cloud.autoconfigure.implementation.context.properties.AzureGlobalProperties;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import dev.albertv.projects.api.core.properties.AzureStorageProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {

    @Bean
    public AzureGlobalProperties azureGlobalProperties() {
        return new AzureGlobalProperties();
    }

    @Bean
    BlobServiceClient blobServiceClient(
        final AzureStorageProperties azureStorageProperties
    ) {
        return new BlobServiceClientBuilder()
            .connectionString(azureStorageProperties.connectionString())
            .buildClient();
    }

}
