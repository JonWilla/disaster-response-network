package com.jonwilla.disasterresponse.assignment.dto;

import com.jonwilla.disasterresponse.assignment.AssignmentStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssignmentResponse(
        UUID id,
        UUID incidentId,
        UUID responderId,
        UUID resourceId,
        AssignmentStatus status,
        OffsetDateTime assignedAt
) {
}