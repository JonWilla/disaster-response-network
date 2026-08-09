package com.jonwilla.disasterresponse.auth;
import com.jonwilla.disasterresponse.exception.DuplicateEmailException;
import com.jonwilla.disasterresponse.auth.dto.AuthenticationResponse;
import com.jonwilla.disasterresponse.auth.dto.LoginRequest;
import com.jonwilla.disasterresponse.auth.dto.RegisterRequest;
import com.jonwilla.disasterresponse.user.UserAccount;
import com.jonwilla.disasterresponse.user.UserRepository;
import com.jonwilla.disasterresponse.user.UserRole;
import com.jonwilla.disasterresponse.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager
            authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.jwtService =
                jwtService;

        this.authenticationManager =
                authenticationManager;
    }

    public AuthenticationResponse register(
            RegisterRequest request
    ) {
        if (
                userRepository.existsByEmail(
                        request.email()
                )
        ) {
            throw new DuplicateEmailException(
                    request.email()
            );
        }

        UserAccount account =
                new UserAccount(
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        passwordEncoder.encode(
                                request.password()
                        ),
                        UserRole.CITIZEN
                );

        UserAccount saved =
                userRepository.save(account);

        UserDetails userDetails =
                User
                        .withUsername(
                                saved.getEmail()
                        )
                        .password(
                                saved.getPasswordHash()
                        )
                        .roles(
                                saved.getRole().name()
                        )
                        .build();

        String token =
                jwtService.generateToken(
                        userDetails
                );

        return new AuthenticationResponse(
                token,
                saved.getId(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    public AuthenticationResponse login(
            LoginRequest request
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserAccount account =
                userRepository
                        .findByEmail(
                                request.email()
                        )
                        .orElseThrow();

        UserDetails userDetails =
                User
                        .withUsername(
                                account.getEmail()
                        )
                        .password(
                                account.getPasswordHash()
                        )
                        .roles(
                                account.getRole().name()
                        )
                        .build();

        String token =
                jwtService.generateToken(
                        userDetails
                );

        return new AuthenticationResponse(
                token,
                account.getId(),
                account.getEmail(),
                account.getRole()
        );
    }
}