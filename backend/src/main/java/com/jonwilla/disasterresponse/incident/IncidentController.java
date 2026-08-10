package com.jonwilla.disasterresponse.incident;

import com.jonwilla.disasterresponse.incident.dto.CreateIncidentRequest;
import com.jonwilla.disasterresponse.incident.dto.IncidentResponse;
import com.jonwilla.disasterresponse.incident.dto.IncidentSummaryResponse;
import com.jonwilla.disasterresponse.incident.dto.UpdateIncidentRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        this.incidentService =
                incidentService;
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
                                "/api/incidents/"
                                        + response.id()
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

    @GetMapping("/summary")
    public IncidentSummaryResponse getSummary() {
        return incidentService.getSummary();
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