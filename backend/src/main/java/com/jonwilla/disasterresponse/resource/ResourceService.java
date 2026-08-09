package com.jonwilla.disasterresponse.resource;

import com.jonwilla.disasterresponse.exception.DomainNotFoundException;
import com.jonwilla.disasterresponse.resource.dto.CreateResourceRequest;
import com.jonwilla.disasterresponse.resource.dto.ResourceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(
            ResourceRepository resourceRepository
    ) {
        this.resourceRepository = resourceRepository;
    }

    public ResourceResponse create(
            CreateResourceRequest request
    ) {
        Resource resource = new Resource(
                request.name(),
                request.type(),
                ResourceStatus.AVAILABLE,
                request.quantity(),
                request.location()
        );

        return toResponse(
                resourceRepository.save(resource)
        );
    }

    @Transactional(readOnly = true)
    public List<ResourceResponse> findAll(
            ResourceStatus status,
            ResourceType type
    ) {
        List<Resource> resources;

        if (status != null) {
            resources =
                    resourceRepository.findByStatus(status);
        } else if (type != null) {
            resources =
                    resourceRepository.findByType(type);
        } else {
            resources =
                    resourceRepository.findAll();
        }

        return resources.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResourceResponse findById(UUID id) {
        return toResponse(findResource(id));
    }

    private Resource findResource(UUID id) {
        return resourceRepository
                .findById(id)
                .orElseThrow(() ->
                        new DomainNotFoundException(
                                "Resource not found with id: " + id
                        )
                );
    }

    private ResourceResponse toResponse(
            Resource resource
    ) {
        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getType(),
                resource.getStatus(),
                resource.getQuantity(),
                resource.getLocation()
        );
    }
}