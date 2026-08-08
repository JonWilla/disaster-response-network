package com.jonwilla.disasterresponse.incident.dto;

import com.jonwilla.disasterresponse.incident.IncidentSeverity;
import com.jonwilla.disasterresponse.incident.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateIncidentRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must not exceed 150 characters")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Severity is required")
        IncidentSeverity severity,

        @NotNull(message = "Status is required")
        IncidentStatus status,

        @NotBlank(message = "Location is required")
        @Size(max = 255, message = "Location must not exceed 255 characters")
        String location
) {
}