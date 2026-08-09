package com.jonwilla.disasterresponse.assignment;

import com.jonwilla.disasterresponse.assignment.dto.AssignmentResponse;
import com.jonwilla.disasterresponse.assignment.dto.CreateAssignmentRequest;
import com.jonwilla.disasterresponse.assignment.dto.UpdateAssignmentStatusRequest;
import com.jonwilla.disasterresponse.exception.DomainNotFoundException;
import com.jonwilla.disasterresponse.incident.Incident;
import com.jonwilla.disasterresponse.incident.IncidentRepository;
import com.jonwilla.disasterresponse.responder.Responder;
import com.jonwilla.disasterresponse.responder.ResponderRepository;
import com.jonwilla.disasterresponse.responder.ResponderStatus;
import com.jonwilla.disasterresponse.resource.Resource;
import com.jonwilla.disasterresponse.resource.ResourceRepository;
import com.jonwilla.disasterresponse.resource.ResourceStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final IncidentRepository incidentRepository;
    private final ResponderRepository responderRepository;
    private final ResourceRepository resourceRepository;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            IncidentRepository incidentRepository,
            ResponderRepository responderRepository,
            ResourceRepository resourceRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.incidentRepository = incidentRepository;
        this.responderRepository = responderRepository;
        this.resourceRepository = resourceRepository;
    }

    public AssignmentResponse create(
            CreateAssignmentRequest request
    ) {
        Incident incident = incidentRepository
                .findById(request.incidentId())
                .orElseThrow(() ->
                        new DomainNotFoundException(
                                "Incident not found with id: "
                                        + request.incidentId()
                        )
                );

        Responder responder = responderRepository
                .findById(request.responderId())
                .orElseThrow(() ->
                        new DomainNotFoundException(
                                "Responder not found with id: "
                                        + request.responderId()
                        )
                );

        Resource resource = null;

        if (request.resourceId() != null) {
            resource = resourceRepository
                    .findById(request.resourceId())
                    .orElseThrow(() ->
                            new DomainNotFoundException(
                                    "Resource not found with id: "
                                            + request.resourceId()
                            )
                    );
        }

        Assignment assignment =
                new Assignment(
                        incident,
                        responder,
                        resource,
                        AssignmentStatus.ASSIGNED
                );

        responder.setStatus(
                ResponderStatus.ASSIGNED
        );

        if (resource != null) {
            resource.setStatus(
                    ResourceStatus.ALLOCATED
            );
        }

        return toResponse(
                assignmentRepository.save(assignment)
        );
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> findAll() {
        return assignmentRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AssignmentResponse updateStatus(
            UUID id,
            UpdateAssignmentStatusRequest request
    ) {
        Assignment assignment =
                assignmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new DomainNotFoundException(
                                        "Assignment not found with id: "
                                                + id
                                )
                        );

        assignment.setStatus(
                request.status()
        );

        return toResponse(
                assignmentRepository.save(assignment)
        );
    }

    private AssignmentResponse toResponse(
            Assignment assignment
    ) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getIncident().getId(),
                assignment.getResponder().getId(),
                assignment.getResource() == null
                        ? null
                        : assignment.getResource().getId(),
                assignment.getStatus(),
                assignment.getAssignedAt()
        );
    }
}