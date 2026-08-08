package com.jonwilla.disasterresponse.incident;

import com.jonwilla.disasterresponse.exception.IncidentNotFoundException;
import com.jonwilla.disasterresponse.incident.dto.CreateIncidentRequest;
import com.jonwilla.disasterresponse.incident.dto.IncidentResponse;
import com.jonwilla.disasterresponse.incident.dto.UpdateIncidentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

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
                            .findByStatusOrderByReportedAtDesc(status);

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
    public IncidentResponse findById(UUID id) {
        Incident incident = findIncident(id);

        return toResponse(incident);
    }

    public IncidentResponse update(
            UUID id,
            UpdateIncidentRequest request
    ) {
        Incident incident = findIncident(id);

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
                incidentRepository.saveAndFlush(incident);

        return toResponse(savedIncident);
    }

    public void delete(UUID id) {
        Incident incident = findIncident(id);

        incidentRepository.delete(incident);
    }

    private Incident findIncident(UUID id) {
        return incidentRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new IncidentNotFoundException(id)
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