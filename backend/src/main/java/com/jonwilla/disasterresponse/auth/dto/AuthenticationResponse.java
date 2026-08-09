package com.jonwilla.disasterresponse.auth.dto;

import com.jonwilla.disasterresponse.user.UserRole;

import java.util.UUID;

public record AuthenticationResponse(
        String token,
        UUID userId,
        String email,
        UserRole role
) {
}