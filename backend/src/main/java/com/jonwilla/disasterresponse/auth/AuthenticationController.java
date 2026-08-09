package com.jonwilla.disasterresponse.auth;

import com.jonwilla.disasterresponse.auth.dto.AuthenticationResponse;
import com.jonwilla.disasterresponse.auth.dto.LoginRequest;
import com.jonwilla.disasterresponse.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService
            authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService
    ) {
        this.authenticationService =
                authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {
        return authenticationService
                .register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {
        return authenticationService
                .login(request);
    }
}