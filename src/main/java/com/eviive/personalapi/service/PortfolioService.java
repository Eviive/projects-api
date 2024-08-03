package com.eviive.personalapi.service;

import com.eviive.personalapi.config.exception.PersonalApiException;
import com.eviive.personalapi.dto.web.RevalidateRequestDTO;
import com.eviive.personalapi.dto.web.RevalidateResponseDTO;
import com.eviive.personalapi.properties.PortfolioPropertiesConfig;
import com.eviive.personalapi.service.web.PortfolioWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioWebService portfolioWebService;

    private final PortfolioPropertiesConfig portfolioPropertiesConfig;

    public void revalidate() {
        final RevalidateRequestDTO revalidateRequest = new RevalidateRequestDTO(
            portfolioPropertiesConfig.api().secret(),
            "/"
        );

        final ResponseEntity<RevalidateResponseDTO> revalidateResponseEntity =
            portfolioWebService.revalidate(revalidateRequest);

        final boolean isError = revalidateResponseEntity.getStatusCode().isError();

        final RevalidateResponseDTO revalidateResponseDTO = revalidateResponseEntity.getBody();

        final boolean isRevalidated = !isError &&
            revalidateResponseDTO != null &&
            Boolean.TRUE.equals(revalidateResponseDTO.revalidated());

        if (isError || !isRevalidated) {
            throw new PersonalApiException(
                "Failed to revalidate portfolio",
                (HttpStatus) revalidateResponseEntity.getStatusCode()
            );
        }
    }

}
