CREATE TABLE incidents
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(150) NOT NULL,
    description TEXT         NOT NULL,
    severity    VARCHAR(20)  NOT NULL,
    status      VARCHAR(30)  NOT NULL,
    location    VARCHAR(255) NOT NULL,
    reported_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_incidents_status
    ON incidents (status);

CREATE INDEX idx_incidents_severity
    ON incidents (severity);

CREATE INDEX idx_incidents_reported_at
    ON incidents (reported_at);