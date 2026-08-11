package com.jonwilla.disasterresponse.websocket;

import com.jonwilla.disasterresponse.incident.IncidentSeverity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LiveIncidentMessage(
        UUID incidentId,
        String title,
        IncidentSeverity severity,
        String location,
        OffsetDateTime reportedAt
) {
}