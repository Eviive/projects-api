package dev.albertv.projects.api.controller;

import dev.albertv.projects.api.core.properties.ProjectsApiProperties;
import dev.albertv.projects.api.dto.ApiInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
@Tag(name = "Info")
public class InfoController {

    private final ProjectsApiProperties projectsApiProperties;

    // GET

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Info",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<ApiInfoDTO> findApiInfo() {
        return ResponseEntity.ok(
            new ApiInfoDTO(
                projectsApiProperties.version(),
                projectsApiProperties.stage()
            )
        );
    }

}
