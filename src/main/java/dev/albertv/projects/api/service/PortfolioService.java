package dev.albertv.projects.api.service;

import dev.albertv.projects.api.core.exception.ProjectsApiException;
import dev.albertv.projects.api.core.properties.PortfolioProperties;
import dev.albertv.projects.api.dto.web.RevalidateRequestDTO;
import dev.albertv.projects.api.dto.web.RevalidateResponseDTO;
import dev.albertv.projects.api.service.web.PortfolioWebService;
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

    private final PortfolioProperties portfolioProperties;

    public void revalidate() {
        final RevalidateRequestDTO revalidateRequest = new RevalidateRequestDTO(
            portfolioProperties.api().secret(),
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
            throw new ProjectsApiException(
                "Failed to revalidate portfolio",
                (HttpStatus) revalidateResponseEntity.getStatusCode()
            );
        }
    }

}
