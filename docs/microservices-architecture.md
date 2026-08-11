# Disaster Response Network — Microservices Architecture

## Current Architecture

The application currently operates as a modular Spring Boot monolith with
PostgreSQL, Redis, Kafka, WebSockets, React, Nginx, and Docker Compose.

The backend domains are already separated into packages, allowing them to be
extracted gradually into independent services without rewriting the entire
application.

## Target Services

### Incident Service

Responsibilities:

- Incident creation
- Incident updates
- Incident status
- Incident severity
- Incident querying
- Incident events

Primary domain:

- Incident

Publishes events:

- IncidentCreatedEvent

---

### Identity Service

Responsibilities:

- User registration
- Authentication
- JWT generation
- User accounts
- Roles
- Authorization identity

Primary domains:

- UserAccount
- UserRole

---

### Dispatch Service

Responsibilities:

- Responders
- Resources
- Assignments
- Incident-response coordination

Primary domains:

- Responder
- Resource
- Assignment

---

### Notification Service

Responsibilities:

- Consume incident events
- Detect critical incidents
- Persist notifications
- Expose notification APIs

Consumes:

- IncidentCreatedEvent

---

### Analytics Service

Responsibilities:

- Incident statistics
- Dashboard summaries
- Operational metrics
- Historical analytics

Consumes domain events rather than directly modifying operational data.

---

## Communication Model

Synchronous communication:

REST APIs

Asynchronous communication:

Apache Kafka

Real-time browser communication:

WebSocket / STOMP

---

## Infrastructure

- PostgreSQL — persistent relational storage
- Redis — distributed caching
- Kafka — event bus
- Docker — service packaging
- Docker Compose — local service orchestration
- Nginx — frontend web server
- React — operations dashboard

## Extraction Strategy

The existing modular monolith will be separated incrementally.

Services will be extracted one at a time while keeping the application
functional after every step.

Initial extraction order:

1. Notification Service
2. Analytics Service
3. Incident Service
4. Dispatch Service
5. Identity Service

Notification and analytics are extracted first because they already consume
events and have weaker coupling to the transactional core.