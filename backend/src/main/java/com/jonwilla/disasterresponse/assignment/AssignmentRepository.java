package com.jonwilla.disasterresponse.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentRepository
        extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByIncidentId(
            UUID incidentId
    );

    List<Assignment> findByResponderId(
            UUID responderId
    );

    List<Assignment> findByStatus(
            AssignmentStatus status
    );
}