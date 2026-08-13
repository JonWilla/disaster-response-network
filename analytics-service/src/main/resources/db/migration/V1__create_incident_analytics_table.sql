CREATE TABLE incident_analytics (
                                    id UUID PRIMARY KEY,
                                    incident_id UUID NOT NULL UNIQUE,
                                    title VARCHAR(150) NOT NULL,
                                    severity VARCHAR(20) NOT NULL,
                                    location VARCHAR(255),
                                    reported_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_incident_analytics_severity
    ON incident_analytics(severity);

CREATE INDEX idx_incident_analytics_reported_at
    ON incident_analytics(reported_at DESC);