package com.jonwilla.notification.notification.dto;

import com.jonwilla.notification.notification.NotificationType;

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