package com.jonwilla.notification.notification;

import com.jonwilla.notification.event.IncidentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(
                    NotificationEventConsumer.class
            );

    private final NotificationService notificationService;

    public NotificationEventConsumer(
            NotificationService notificationService
    ) {
        this.notificationService =
                notificationService;
    }

    @KafkaListener(
            topics = "incident-events",
            groupId = "notification-service-group"
    )
    public void consume(
            IncidentCreatedEvent event
    ) {

        if (!"CRITICAL".equalsIgnoreCase(event.severity())) {
            return;
        }

        notificationService
                .createCriticalIncidentNotification(
                        event.incidentId(),
                        event.title(),
                        event.location()
                );

        log.info(
                "Critical incident notification created by notification-service: incidentId={}, title={}",
                event.incidentId(),
                event.title()
        );
    }
}