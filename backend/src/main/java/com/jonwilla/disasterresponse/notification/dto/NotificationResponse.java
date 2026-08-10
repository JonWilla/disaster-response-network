package com.jonwilla.disasterresponse.notification.dto;

import com.jonwilla.disasterresponse.notification.NotificationType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID incidentId,
        NotificationType type,
        String message,
        boolean read,
        OffsetDateTime createdAt
) {
}