package com.jonwilla.disasterresponse.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTest {

    @Test
    void bcryptShouldHashAndVerifyPassword() {

        PasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String rawPassword =
                "StrongPass123!";

        String encodedPassword =
                encoder.encode(
                        rawPassword
                );

        assertNotEquals(
                rawPassword,
                encodedPassword
        );

        assertTrue(
                encoder.matches(
                        rawPassword,
                        encodedPassword
                )
        );
    }
}