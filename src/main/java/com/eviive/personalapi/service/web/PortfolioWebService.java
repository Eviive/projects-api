package com.eviive.personalapi.service.web;

import com.eviive.personalapi.dto.web.RevalidateRequestDTO;
import com.eviive.personalapi.dto.web.RevalidateResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PortfolioWebService {

    @PostExchange("/revalidate")
    ResponseEntity<RevalidateResponseDTO> revalidate(
        @RequestBody @Valid RevalidateRequestDTO revalidateRequestDTO
    );

}
