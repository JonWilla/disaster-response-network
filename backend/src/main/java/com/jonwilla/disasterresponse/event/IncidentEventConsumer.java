package com.jonwilla.disasterresponse.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(
                    IncidentEventConsumer.class
            );

    @KafkaListener(
            topics = IncidentEventProducer.TOPIC,
            groupId = "disaster-response-group"
    )
    public void consume(
            IncidentCreatedEvent event
    ) {
        log.info(
                "Kafka event received: incidentId={}, title={}, severity={}, location={}",
                event.incidentId(),
                event.title(),
                event.severity(),
                event.location()
        );
    }
}