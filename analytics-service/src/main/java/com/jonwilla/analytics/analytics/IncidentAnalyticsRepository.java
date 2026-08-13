package com.jonwilla.analytics.analytics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncidentAnalyticsRepository
        extends JpaRepository<IncidentAnalytics, UUID> {

    boolean existsByIncidentId(UUID incidentId);

    long countBySeverity(String severity);
}