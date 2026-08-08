package com.jonwilla.disasterresponse.incident;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    List<Incident> findAllByOrderByReportedAtDesc();

    List<Incident> findByStatusOrderByReportedAtDesc(IncidentStatus status);

    List<Incident> findBySeverityOrderByReportedAtDesc(
            IncidentSeverity severity
    );
}