package com.jonwilla.disasterresponse.assignment.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAssignmentRequest(

        @NotNull(message = "Incident ID is required")
        UUID incidentId,

        @NotNull(message = "Responder ID is required")
        UUID responderId,

        UUID resourceId
) {
}