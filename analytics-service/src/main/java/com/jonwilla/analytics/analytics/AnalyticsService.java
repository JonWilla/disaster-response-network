package com.jonwilla.analytics.analytics;

import com.jonwilla.analytics.analytics.dto.AnalyticsSummaryResponse;
import com.jonwilla.analytics.event.IncidentCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnalyticsService {

    private final IncidentAnalyticsRepository repository;

    public AnalyticsService(
            IncidentAnalyticsRepository repository
    ) {
        this.repository = repository;
    }

    public void recordIncident(
            IncidentCreatedEvent event
    ) {
        if (repository.existsByIncidentId(event.incidentId())) {
            return;
        }

        IncidentAnalytics analytics =
                new IncidentAnalytics(
                        event.incidentId(),
                        event.title(),
                        event.severity(),
                        event.location(),
                        event.reportedAt()
                );

        repository.save(analytics);
    }

    @Transactional(readOnly = true)
    public AnalyticsSummaryResponse getSummary() {

        return new AnalyticsSummaryResponse(
                repository.count(),
                repository.countBySeverity("CRITICAL"),
                repository.countBySeverity("HIGH"),
                repository.countBySeverity("MEDIUM"),
                repository.countBySeverity("LOW")
        );
    }
}