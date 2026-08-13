CREATE TABLE notifications (
                               id UUID PRIMARY KEY,
                               incident_id UUID NOT NULL,
                               type VARCHAR(50) NOT NULL,
                               message VARCHAR(500) NOT NULL,
                               is_read BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_notifications_created_at
    ON notifications(created_at DESC);

CREATE INDEX idx_notifications_incident_id
    ON notifications(incident_id);

CREATE INDEX idx_notifications_is_read
    ON notifications(is_read);