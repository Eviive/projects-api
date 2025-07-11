package com.eviive.personalapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.eviive.personalapi.core.exception.PersonalApiException;
import com.eviive.personalapi.core.properties.AzureStoragePropertiesConfig;
import com.eviive.personalapi.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API400_FILE_EMPTY;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API400_IMAGE_NO_NAME;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API415_FILE_NOT_IMAGE;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API500_UNKNOWN_CONTAINER;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API500_UPLOAD_ERROR;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final BlobServiceClient blobServiceClient;

    private final AzureStoragePropertiesConfig azureStoragePropertiesConfig;

    @Transactional(propagation = MANDATORY)
    public void upload(final Image image, final UUID oldUuid, final MultipartFile file) {
        if (file.isEmpty()) {
            throw new PersonalApiException(API400_FILE_EMPTY);
        }

        final String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw PersonalApiException.format(API415_FILE_NOT_IMAGE, file.getContentType());
        }

        if (file.getOriginalFilename() == null) {
            throw new PersonalApiException(API400_IMAGE_NO_NAME);
        }

        final BlobClient blobClient = getBlobClient(image);

        try {
            blobClient.upload(file.getInputStream(), file.getSize());

            final BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());

            blobClient.setHttpHeaders(headers);
        } catch (IOException e) {
            throw PersonalApiException.format(e, API500_UPLOAD_ERROR, e.getMessage());
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
            containerName = azureStoragePropertiesConfig.blob().projects().containerName();
        } else if (image.getSkill() != null) {
            containerName = azureStoragePropertiesConfig.blob().skills().containerName();
        } else {
            throw PersonalApiException.format(API500_UNKNOWN_CONTAINER, image.getUuid().toString());
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
