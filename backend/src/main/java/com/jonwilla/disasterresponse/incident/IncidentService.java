package com.jonwilla.disasterresponse.incident;

import com.jonwilla.disasterresponse.event.IncidentCreatedEvent;
import com.jonwilla.disasterresponse.event.IncidentEventProducer;
import com.jonwilla.disasterresponse.exception.IncidentNotFoundException;
import com.jonwilla.disasterresponse.incident.dto.CreateIncidentRequest;
import com.jonwilla.disasterresponse.incident.dto.IncidentResponse;
import com.jonwilla.disasterresponse.incident.dto.IncidentSummaryResponse;
import com.jonwilla.disasterresponse.incident.dto.UpdateIncidentRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentEventProducer incidentEventProducer;

    public IncidentService(
            IncidentRepository incidentRepository,
            IncidentEventProducer incidentEventProducer
    ) {
        this.incidentRepository =
                incidentRepository;

        this.incidentEventProducer =
                incidentEventProducer;
    }

    @CacheEvict(
            value = "incidentSummary",
            allEntries = true
    )
    public IncidentResponse create(
            CreateIncidentRequest request
    ) {
        Incident incident = new Incident(
                request.title(),
                request.description(),
                request.severity(),
                IncidentStatus.REPORTED,
                request.location()
        );

        Incident savedIncident =
                incidentRepository.save(incident);

        incidentEventProducer.publishIncidentCreated(
                new IncidentCreatedEvent(
                        savedIncident.getId(),
                        savedIncident.getTitle(),
                        savedIncident.getSeverity(),
                        savedIncident.getLocation(),
                        savedIncident.getReportedAt()
                )
        );

        return toResponse(savedIncident);
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> findAll(
            IncidentStatus status,
            IncidentSeverity severity
    ) {
        List<Incident> incidents;

        if (status != null) {

            incidents =
                    incidentRepository
                            .findByStatusOrderByReportedAtDesc(
                                    status
                            );

        } else if (severity != null) {

            incidents =
                    incidentRepository
                            .findBySeverityOrderByReportedAtDesc(
                                    severity
                            );

        } else {

            incidents =
                    incidentRepository
                            .findAllByOrderByReportedAtDesc();
        }

        return incidents.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public IncidentResponse findById(
            UUID id
    ) {
        Incident incident =
                findIncident(id);

        return toResponse(incident);
    }

    @Cacheable("incidentSummary")
    @Transactional(readOnly = true)
    public IncidentSummaryResponse getSummary() {

        long total =
                incidentRepository.count();

        long reported =
                incidentRepository.countByStatus(
                        IncidentStatus.REPORTED
                );

        long inProgress =
                incidentRepository.countByStatus(
                        IncidentStatus.IN_PROGRESS
                );

        long resolved =
                incidentRepository.countByStatus(
                        IncidentStatus.RESOLVED
                );

        long critical =
                incidentRepository.countBySeverity(
                        IncidentSeverity.CRITICAL
                );

        return new IncidentSummaryResponse(
                total,
                reported,
                inProgress,
                resolved,
                critical
        );
    }

    @CacheEvict(
            value = "incidentSummary",
            allEntries = true
    )
    public IncidentResponse update(
            UUID id,
            UpdateIncidentRequest request
    ) {
        Incident incident =
                findIncident(id);

        incident.setTitle(
                request.title()
        );

        incident.setDescription(
                request.description()
        );

        incident.setSeverity(
                request.severity()
        );

        incident.setStatus(
                request.status()
        );

        incident.setLocation(
                request.location()
        );

        Incident savedIncident =
                incidentRepository
                        .saveAndFlush(incident);

        return toResponse(savedIncident);
    }

    @CacheEvict(
            value = "incidentSummary",
            allEntries = true
    )
    public void delete(
            UUID id
    ) {
        Incident incident =
                findIncident(id);

        incidentRepository.delete(
                incident
        );
    }

    private Incident findIncident(
            UUID id
    ) {
        return incidentRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new IncidentNotFoundException(
                                        id
                                )
                );
    }

    private IncidentResponse toResponse(
            Incident incident
    ) {
        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getLocation(),
                incident.getReportedAt(),
                incident.getUpdatedAt()
        );
    }
}