package com.jonwilla.analytics.analytics;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "incident_analytics")
public class IncidentAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "incident_id", nullable = false, unique = true)
    private UUID incidentId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 20)
    private String severity;

    @Column(length = 255)
    private String location;

    @Column(name = "reported_at", nullable = false)
    private OffsetDateTime reportedAt;

    protected IncidentAnalytics() {
    }

    public IncidentAnalytics(
            UUID incidentId,
            String title,
            String severity,
            String location,
            OffsetDateTime reportedAt
    ) {
        this.incidentId = incidentId;
        this.title = title;
        this.severity = severity;
        this.location = location;
        this.reportedAt = reportedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIncidentId() {
        return incidentId;
    }

    public String getTitle() {
        return title;
    }

    public String getSeverity() {
        return severity;
    }

    public String getLocation() {
        return location;
    }

    public OffsetDateTime getReportedAt() {
        return reportedAt;
    }
}