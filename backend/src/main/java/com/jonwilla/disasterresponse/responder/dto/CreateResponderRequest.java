package com.jonwilla.disasterresponse.responder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateResponderRequest(

        @NotNull(message = "User ID is required")
        UUID userId,

        @NotBlank(message = "Specialization is required")
        String specialization,

        String phoneNumber

) {
}