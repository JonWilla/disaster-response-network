package com.jonwilla.analytics.event;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IncidentCreatedEvent(
        UUID incidentId,
        String title,
        String severity,
        String location,
        OffsetDateTime reportedAt
) {
}