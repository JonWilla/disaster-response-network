CREATE TABLE user_accounts
(
    id            UUID PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(30)  NOT NULL,
    enabled       BOOLEAN      NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_user_accounts_email
    ON user_accounts (email);

CREATE INDEX idx_user_accounts_role
    ON user_accounts (role);


CREATE TABLE responders
(
    id             UUID PRIMARY KEY,
    user_id        UUID         NOT NULL UNIQUE,
    specialization VARCHAR(120) NOT NULL,
    phone_number   VARCHAR(40),
    status         VARCHAR(30)  NOT NULL,

    CONSTRAINT fk_responders_user
        FOREIGN KEY (user_id)
            REFERENCES user_accounts (id)
);

CREATE INDEX idx_responders_status
    ON responders (status);


CREATE TABLE resources
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(150) NOT NULL,
    type     VARCHAR(50)  NOT NULL,
    status   VARCHAR(30)  NOT NULL,
    quantity INTEGER      NOT NULL,
    location VARCHAR(255) NOT NULL,

    CONSTRAINT chk_resources_quantity
        CHECK (quantity >= 0)
);

CREATE INDEX idx_resources_type
    ON resources (type);

CREATE INDEX idx_resources_status
    ON resources (status);


CREATE TABLE assignments
(
    id           UUID PRIMARY KEY,
    incident_id  UUID        NOT NULL,
    responder_id UUID        NOT NULL,
    resource_id  UUID,
    status       VARCHAR(30) NOT NULL,
    assigned_at  TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_assignments_incident
        FOREIGN KEY (incident_id)
            REFERENCES incidents (id),

    CONSTRAINT fk_assignments_responder
        FOREIGN KEY (responder_id)
            REFERENCES responders (id),

    CONSTRAINT fk_assignments_resource
        FOREIGN KEY (resource_id)
            REFERENCES resources (id)
);

CREATE INDEX idx_assignments_incident
    ON assignments (incident_id);

CREATE INDEX idx_assignments_responder
    ON assignments (responder_id);

CREATE INDEX idx_assignments_status
    ON assignments (status);