package dev.albertv.projects.api.core.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "projects-api.azure.storage.blob")
@Validated
public record AzureStorageBlobProperties(
    @NotBlank(message = "The Azure Blob Storage endpoint must be specified.")
    String endpoint,
    @NotNull(message = "The Azure Storage Blob project container properties must be specified.")
    Container projects,
    @NotNull(message = "The Azure Storage Blob skill container properties must be specified.")
    Container skills
) {

    public record Container(
        @NotBlank(message = "The Azure Storage Blob container name must be specified.")
        String containerName
    ) {

    }

}
