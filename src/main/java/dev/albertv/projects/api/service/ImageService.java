package dev.albertv.projects.api.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import dev.albertv.projects.api.core.exception.ProjectsApiException;
import dev.albertv.projects.api.core.properties.AzureStorageBlobProperties;
import dev.albertv.projects.api.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API400_FILE_EMPTY;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API400_IMAGE_NO_NAME;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API415_FILE_NOT_IMAGE;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API500_UNKNOWN_CONTAINER;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API500_UPLOAD_ERROR;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final BlobServiceClient blobServiceClient;

    private final AzureStorageBlobProperties azureStorageBlobProperties;

    @Transactional(propagation = MANDATORY)
    public void upload(final Image image, final UUID oldUuid, final MultipartFile file) {
        if (file.isEmpty()) {
            throw new ProjectsApiException(API400_FILE_EMPTY);
        }

        final String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw ProjectsApiException.format(API415_FILE_NOT_IMAGE, file.getContentType());
        }

        if (file.getOriginalFilename() == null) {
            throw new ProjectsApiException(API400_IMAGE_NO_NAME);
        }

        final BlobClient blobClient = getBlobClient(image);

        try {
            blobClient.upload(file.getInputStream(), file.getSize());

            final BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());

            blobClient.setHttpHeaders(headers);
        } catch (IOException e) {
            throw ProjectsApiException.format(e, API500_UPLOAD_ERROR, e.getMessage());
        }

        if (oldUuid != null) {
            getBlobClient(image, oldUuid).deleteIfExists();
        }
    }

    public void delete(final Image image) {
        getBlobClient(image).deleteIfExists();
    }

    private BlobContainerClient getBlobContainerClient(final Image image) {
        final String containerName;

        if (image.getProject() != null) {
            containerName = azureStorageBlobProperties.projects().containerName();
        } else if (image.getSkill() != null) {
            containerName = azureStorageBlobProperties.skills().containerName();
        } else {
            throw ProjectsApiException.format(API500_UNKNOWN_CONTAINER, image.getUuid().toString());
        }

        return blobServiceClient.getBlobContainerClient(containerName);
    }

    private BlobClient getBlobClient(final Image image, final UUID oldUuid) {
        return getBlobContainerClient(image).getBlobClient(oldUuid.toString());
    }

    private BlobClient getBlobClient(final Image image) {
        return getBlobClient(image, image.getUuid());
    }

}
