package dev.albertv.projects.api.service.web;

import dev.albertv.projects.api.dto.web.RevalidateRequestDTO;
import dev.albertv.projects.api.dto.web.RevalidateResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PortfolioWebService {

    @PostExchange("/revalidate")
    ResponseEntity<RevalidateResponseDTO> revalidate(
        @RequestBody RevalidateRequestDTO revalidateRequestDTO
    );

}
