package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.dto.ProjectLightDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.service.ProjectService;
import com.eviive.personalapi.util.UriUtilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    private final UriUtilities uriUtilities;

    // GET

    @GetMapping(path = "page", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Find a page of projects",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<Page<ProjectDTO>> findAll(
        @SortDefault("sort") final Pageable pageable,
        @RequestParam(required = false) final String search
    ) {
        return ResponseEntity.ok(projectService.findAll(pageable, search));
    }

    @GetMapping(path = "light", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Find all light projects",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<List<ProjectLightDTO>> findAllLight() {
        return ResponseEntity.ok(projectService.findAllLight());
    }

    @GetMapping(path = "featured", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Find all featured projects",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<List<ProjectDTO>> findAllFeatured() {
        return ResponseEntity.ok(projectService.findAllFeatured());
    }

    @GetMapping(path = "not-featured", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Find a page of not featured projects",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<Page<ProjectDTO>> findAllNotFeatured(@SortDefault("sort") final Pageable pageable) {
        return ResponseEntity.ok(projectService.findAllNotFeatured(pageable));
    }

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a project",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
        }
    )
    public ResponseEntity<ProjectDTO> save(@RequestBody @Valid final ProjectDTO projectDTO) {
        final ProjectDTO createdProject = projectService.create(projectDTO, null);
        final URI location = uriUtilities.buildLocation(createdProject.id());
        return ResponseEntity.created(location)
            .body(createdProject);
    }

    @PostMapping(path = "with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a project with an image",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                responseCode = "415",
                description = "Unsupported Media Type",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
        }
    )
    public ResponseEntity<ProjectDTO> saveWithImage(
        @RequestPart("project") @Valid final ProjectDTO projectDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        final ProjectDTO createdProject = projectService.create(projectDTO, file);
        final URI location = uriUtilities.buildLocation(createdProject.id(), "with-image");
        return ResponseEntity.created(location)
            .body(createdProject);
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update a project",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
        }
    )
    public ResponseEntity<ProjectDTO> update(
        @PathVariable final Long id,
        @RequestBody @Valid final ProjectDTO projectDTO
    ) {
        return ResponseEntity.ok(projectService.update(id, projectDTO, null));
    }

    @PutMapping(path = "{id}/with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update a project with an image",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                responseCode = "415",
                description = "Unsupported Media Type",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
        }
    )
    public ResponseEntity<ProjectDTO> updateWithImage(
        @PathVariable final Long id,
        @RequestPart("project") final @Valid ProjectDTO projectDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        return ResponseEntity.ok(projectService.update(id, projectDTO, file));
    }

    // PATCH

    @PatchMapping(path = "sort", consumes = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Sort projects",
        responses = @ApiResponse(responseCode = "204", description = "No Content")
    )
    public ResponseEntity<Void> sort(@RequestBody final List<SortUpdateDTO> sorts) {
        projectService.sort(sorts);
        return ResponseEntity.noContent().build();
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    @Operation(
        summary = "Delete a project",
        responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(
                responseCode = "404",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
        }
    )
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
