package com.jonwilla.disasterresponse.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMilliseconds;

    public JwtService(
            @Value("${security.jwt.secret}")
            String secret,

            @Value("${security.jwt.expiration-ms}")
            long expirationMilliseconds
    ) {
        this.signingKey =
                Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(secret)
                );

        this.expirationMilliseconds =
                expirationMilliseconds;
    }

    public String generateToken(
            UserDetails userDetails
    ) {
        Date now = new Date();

        Date expiration =
                new Date(
                        now.getTime()
                                + expirationMilliseconds
                );

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(
            String token
    ) {
        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        String username =
                extractUsername(token);

        return username.equals(
                userDetails.getUsername()
        ) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(
            String token
    ) {
        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(
            String token
    ) {
        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {
        Claims claims =
                Jwts.parser()
                        .verifyWith(signingKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return resolver.apply(claims);
    }
}