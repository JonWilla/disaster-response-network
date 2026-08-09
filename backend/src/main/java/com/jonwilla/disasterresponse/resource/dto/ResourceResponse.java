package com.jonwilla.disasterresponse.resource.dto;

import com.jonwilla.disasterresponse.resource.ResourceStatus;
import com.jonwilla.disasterresponse.resource.ResourceType;

import java.util.UUID;

public record ResourceResponse(
        UUID id,
        String name,
        ResourceType type,
        ResourceStatus status,
        int quantity,
        String location
) {
}