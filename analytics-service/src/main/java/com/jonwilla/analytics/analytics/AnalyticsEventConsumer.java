package com.jonwilla.analytics.analytics;

import com.jonwilla.analytics.event.IncidentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(
                    AnalyticsEventConsumer.class
            );

    private final AnalyticsService analyticsService;

    public AnalyticsEventConsumer(
            AnalyticsService analyticsService
    ) {
        this.analyticsService = analyticsService;
    }

    @KafkaListener(
            topics = "incident-events",
            groupId = "analytics-service-group"
    )
    public void consume(
            IncidentCreatedEvent event
    ) {

        analyticsService.recordIncident(event);

        log.info(
                "Analytics recorded incident: incidentId={}, title={}, severity={}",
                event.incidentId(),
                event.title(),
                event.severity()
        );
    }
}