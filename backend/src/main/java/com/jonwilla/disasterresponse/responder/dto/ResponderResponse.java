package com.jonwilla.disasterresponse.responder.dto;

import com.jonwilla.disasterresponse.responder.ResponderStatus;

import java.util.UUID;

public record ResponderResponse(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String specialization,
        String phoneNumber,
        ResponderStatus status
) {
}