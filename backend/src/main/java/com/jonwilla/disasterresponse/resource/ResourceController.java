package com.jonwilla.disasterresponse.resource;

import com.jonwilla.disasterresponse.resource.dto.CreateResourceRequest;
import com.jonwilla.disasterresponse.resource.dto.ResourceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(
            ResourceService resourceService
    ) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> create(
            @Valid
            @RequestBody
            CreateResourceRequest request
    ) {
        ResourceResponse response =
                resourceService.create(request);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/resources/"
                                        + response.id()
                        )
                )
                .body(response);
    }

    @GetMapping
    public List<ResourceResponse> findAll(
            @RequestParam(required = false)
            ResourceStatus status,

            @RequestParam(required = false)
            ResourceType type
    ) {
        return resourceService.findAll(
                status,
                type
        );
    }

    @GetMapping("/{id}")
    public ResourceResponse findById(
            @PathVariable UUID id
    ) {
        return resourceService.findById(id);
    }
}