package com.jonwilla.disasterresponse.incident.dto;

import com.jonwilla.disasterresponse.incident.IncidentSeverity;
import com.jonwilla.disasterresponse.incident.IncidentStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        String title,
        String description,
        IncidentSeverity severity,
        IncidentStatus status,
        String location,
        OffsetDateTime reportedAt,
        OffsetDateTime updatedAt
) {
}