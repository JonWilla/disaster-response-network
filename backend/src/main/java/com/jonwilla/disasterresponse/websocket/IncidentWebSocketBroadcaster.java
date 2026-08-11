package com.jonwilla.disasterresponse.websocket;

import com.jonwilla.disasterresponse.event.IncidentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class IncidentWebSocketBroadcaster {

    private static final Logger log =
            LoggerFactory.getLogger(
                    IncidentWebSocketBroadcaster.class
            );

    private final SimpMessagingTemplate messagingTemplate;

    public IncidentWebSocketBroadcaster(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate =
                messagingTemplate;
    }

    @KafkaListener(
            topics = "incident-events",
            groupId = "websocket-service-group"
    )
    public void broadcast(
            IncidentCreatedEvent event
    ) {

        LiveIncidentMessage message =
                new LiveIncidentMessage(
                        event.incidentId(),
                        event.title(),
                        event.severity(),
                        event.location(),
                        event.reportedAt()
                );

        messagingTemplate.convertAndSend(
                "/topic/incidents",
                message
        );

        log.info(
                "WebSocket incident broadcast sent: incidentId={}, title={}",
                event.incidentId(),
                event.title()
        );
    }
}