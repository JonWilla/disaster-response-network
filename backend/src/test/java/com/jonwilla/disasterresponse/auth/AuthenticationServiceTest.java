package com.jonwilla.disasterresponse.auth;

import com.jonwilla.disasterresponse.auth.dto.AuthenticationResponse;
import com.jonwilla.disasterresponse.auth.dto.LoginRequest;
import com.jonwilla.disasterresponse.auth.dto.RegisterRequest;
import com.jonwilla.disasterresponse.exception.DuplicateEmailException;
import com.jonwilla.disasterresponse.security.JwtService;
import com.jonwilla.disasterresponse.user.UserAccount;
import com.jonwilla.disasterresponse.user.UserRepository;
import com.jonwilla.disasterresponse.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService =
                new AuthenticationService(
                        userRepository,
                        passwordEncoder,
                        jwtService,
                        authenticationManager
                );
    }

    @Test
    void registerShouldCreateCitizenAndReturnToken() {

        RegisterRequest request =
                new RegisterRequest(
                        "Jonny",
                        "Williams",
                        "jonny@example.com",
                        "StrongPass123!"
                );

        when(
                userRepository.existsByEmail(
                        request.email()
                )
        ).thenReturn(false);

        when(
                passwordEncoder.encode(
                        request.password()
                )
        ).thenReturn("hashed-password");

        when(
                userRepository.save(
                        any(UserAccount.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        when(
                jwtService.generateToken(any())
        ).thenReturn("test-jwt-token");

        AuthenticationResponse response =
                authenticationService.register(
                        request
                );

        assertEquals(
                "test-jwt-token",
                response.token()
        );

        assertEquals(
                "jonny@example.com",
                response.email()
        );

        assertEquals(
                UserRole.CITIZEN,
                response.role()
        );

        verify(passwordEncoder)
                .encode("StrongPass123!");

        verify(userRepository)
                .save(any(UserAccount.class));
    }

    @Test
    void registerShouldRejectDuplicateEmail() {

        RegisterRequest request =
                new RegisterRequest(
                        "Jonny",
                        "Williams",
                        "jonny@example.com",
                        "StrongPass123!"
                );

        when(
                userRepository.existsByEmail(
                        request.email()
                )
        ).thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () ->
                        authenticationService
                                .register(request)
        );
    }

    @Test
    void loginShouldAuthenticateAndReturnToken() {

        LoginRequest request =
                new LoginRequest(
                        "jonny@example.com",
                        "StrongPass123!"
                );

        UserAccount account =
                new UserAccount(
                        "Jonny",
                        "Williams",
                        "jonny@example.com",
                        "hashed-password",
                        UserRole.CITIZEN
                );

        when(
                userRepository.findByEmail(
                        request.email()
                )
        ).thenReturn(
                Optional.of(account)
        );

        when(
                jwtService.generateToken(any())
        ).thenReturn("login-jwt-token");

        AuthenticationResponse response =
                authenticationService.login(
                        request
                );

        assertEquals(
                "login-jwt-token",
                response.token()
        );

        assertEquals(
                "jonny@example.com",
                response.email()
        );

        assertEquals(
                UserRole.CITIZEN,
                response.role()
        );

        verify(authenticationManager)
                .authenticate(
                        any(
                                UsernamePasswordAuthenticationToken.class
                        )
                );
    }
}