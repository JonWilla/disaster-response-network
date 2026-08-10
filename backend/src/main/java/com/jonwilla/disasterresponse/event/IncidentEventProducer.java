package com.jonwilla.disasterresponse.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class IncidentEventProducer {

    public static final String TOPIC =
            "incident-events";

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    public IncidentEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.kafkaTemplate =
                kafkaTemplate;
    }

    public void publishIncidentCreated(
            IncidentCreatedEvent event
    ) {
        kafkaTemplate.send(
                TOPIC,
                event.incidentId().toString(),
                event
        );
    }
}