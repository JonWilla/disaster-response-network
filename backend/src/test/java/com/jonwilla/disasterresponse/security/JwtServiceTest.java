package com.jonwilla.disasterresponse.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {

        String secret =
                "VGhpcy1pcy1hLWRldmVsb3BtZW50LWtleS10aGF0LW11c3QtYmUtcmVwbGFjZWQtYmVmb3JlLXByb2R1Y3Rpb24=";

        jwtService =
                new JwtService(
                        secret,
                        3_600_000
                );
    }

    @Test
    void generateTokenShouldContainUsername() {

        UserDetails user =
                User.withUsername(
                                "jonny@example.com"
                        )
                        .password("password")
                        .roles("CITIZEN")
                        .build();

        String token =
                jwtService.generateToken(user);

        String username =
                jwtService.extractUsername(token);

        assertEquals(
                "jonny@example.com",
                username
        );
    }

    @Test
    void generatedTokenShouldBeValidForCorrectUser() {

        UserDetails user =
                User.withUsername(
                                "jonny@example.com"
                        )
                        .password("password")
                        .roles("CITIZEN")
                        .build();

        String token =
                jwtService.generateToken(user);

        assertTrue(
                jwtService.isTokenValid(
                        token,
                        user
                )
        );
    }

    @Test
    void tokenShouldBeInvalidForDifferentUser() {

        UserDetails originalUser =
                User.withUsername(
                                "jonny@example.com"
                        )
                        .password("password")
                        .roles("CITIZEN")
                        .build();

        UserDetails differentUser =
                User.withUsername(
                                "different@example.com"
                        )
                        .password("password")
                        .roles("CITIZEN")
                        .build();

        String token =
                jwtService.generateToken(
                        originalUser
                );

        assertFalse(
                jwtService.isTokenValid(
                        token,
                        differentUser
                )
        );
    }
}