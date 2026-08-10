package com.jonwilla.disasterresponse.event;

import com.jonwilla.disasterresponse.incident.IncidentSeverity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IncidentCreatedEvent(
        UUID incidentId,
        String title,
        IncidentSeverity severity,
        String location,
        OffsetDateTime reportedAt
) {
}