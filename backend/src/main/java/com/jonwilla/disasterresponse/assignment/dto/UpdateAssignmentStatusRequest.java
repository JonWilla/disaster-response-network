package com.jonwilla.disasterresponse.assignment.dto;

import com.jonwilla.disasterresponse.assignment.AssignmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAssignmentStatusRequest(

        @NotNull(message = "Assignment status is required")
        AssignmentStatus status

) {
}