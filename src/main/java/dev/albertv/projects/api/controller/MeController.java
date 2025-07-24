package dev.albertv.projects.api.controller;

import dev.albertv.projects.api.dto.CurrentUserDTO;
import dev.albertv.projects.api.service.MeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@Tag(name = "Me")
public class MeController {

    private final MeService service;

    // GET

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Me",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public CurrentUserDTO findMe() {
        return service.findMe();
    }

}
