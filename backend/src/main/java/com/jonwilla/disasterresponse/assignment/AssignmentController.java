package com.jonwilla.disasterresponse.assignment;

import com.jonwilla.disasterresponse.assignment.dto.AssignmentResponse;
import com.jonwilla.disasterresponse.assignment.dto.CreateAssignmentRequest;
import com.jonwilla.disasterresponse.assignment.dto.UpdateAssignmentStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(
            AssignmentService assignmentService
    ) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> create(
            @Valid
            @RequestBody
            CreateAssignmentRequest request
    ) {
        AssignmentResponse response =
                assignmentService.create(request);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/assignments/"
                                        + response.id()
                        )
                )
                .body(response);
    }

    @GetMapping
    public List<AssignmentResponse> findAll() {
        return assignmentService.findAll();
    }

    @PatchMapping("/{id}/status")
    public AssignmentResponse updateStatus(
            @PathVariable UUID id,

            @Valid
            @RequestBody
            UpdateAssignmentStatusRequest request
    ) {
        return assignmentService.updateStatus(
                id,
                request
        );
    }
}