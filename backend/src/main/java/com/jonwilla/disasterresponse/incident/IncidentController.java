package com.jonwilla.disasterresponse.incident;

import com.jonwilla.disasterresponse.incident.dto.CreateIncidentRequest;
import com.jonwilla.disasterresponse.incident.dto.IncidentResponse;
import com.jonwilla.disasterresponse.incident.dto.UpdateIncidentRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(
            IncidentService incidentService
    ) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> create(
            @Valid
            @RequestBody
            CreateIncidentRequest request
    ) {
        IncidentResponse response =
                incidentService.create(request);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/incidents/" +
                                        response.id()
                        )
                )
                .body(response);
    }

    @GetMapping
    public List<IncidentResponse> findAll(
            @RequestParam(required = false)
            IncidentStatus status,

            @RequestParam(required = false)
            IncidentSeverity severity
    ) {
        return incidentService.findAll(
                status,
                severity
        );
    }

    @GetMapping("/{id}")
    public IncidentResponse findById(
            @PathVariable UUID id
    ) {
        return incidentService.findById(id);
    }

    @PutMapping("/{id}")
    public IncidentResponse update(
            @PathVariable UUID id,

            @Valid
            @RequestBody
            UpdateIncidentRequest request
    ) {
        return incidentService.update(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        incidentService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}