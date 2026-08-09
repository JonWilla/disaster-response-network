package com.jonwilla.disasterresponse.resource.dto;

import com.jonwilla.disasterresponse.resource.ResourceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateResourceRequest(

        @NotBlank(message = "Resource name is required")
        String name,

        @NotNull(message = "Resource type is required")
        ResourceType type,

        @Min(
                value = 0,
                message = "Quantity cannot be negative"
        )
        int quantity,

        @NotBlank(message = "Location is required")
        String location
) {
}