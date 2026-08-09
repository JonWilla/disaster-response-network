package com.jonwilla.disasterresponse.assignment;

import com.jonwilla.disasterresponse.incident.Incident;
import com.jonwilla.disasterresponse.responder.Responder;
import com.jonwilla.disasterresponse.resource.Resource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @UuidGenerator
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "incident_id",
            nullable = false
    )
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "responder_id",
            nullable = false
    )
    private Responder responder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "resource_id"
    )
    private Resource resource;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private AssignmentStatus status;

    @Column(
            name = "assigned_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime assignedAt;

    protected Assignment() {
    }

    public Assignment(
            Incident incident,
            Responder responder,
            Resource resource,
            AssignmentStatus status
    ) {
        this.incident = incident;
        this.responder = responder;
        this.resource = resource;
        this.status = status;
    }

    @PrePersist
    private void beforeInsert() {
        if (assignedAt == null) {
            assignedAt =
                    OffsetDateTime.now(
                            ZoneOffset.UTC
                    );
        }

        if (status == null) {
            status =
                    AssignmentStatus.ASSIGNED;
        }
    }

    public UUID getId() {
        return id;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(
            Incident incident
    ) {
        this.incident = incident;
    }

    public Responder getResponder() {
        return responder;
    }

    public void setResponder(
            Responder responder
    ) {
        this.responder = responder;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(
            Resource resource
    ) {
        this.resource = resource;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(
            AssignmentStatus status
    ) {
        this.status = status;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }
}