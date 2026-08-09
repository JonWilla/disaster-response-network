package com.jonwilla.disasterresponse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    private final CustomUserDetailsService
            userDetailsService;

    private final RestAuthenticationEntryPoint
            authenticationEntryPoint;

    private final RestAccessDeniedHandler
            accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;

        this.userDetailsService =
                userDetailsService;

        this.authenticationEntryPoint =
                authenticationEntryPoint;

        this.accessDeniedHandler =
                accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration
                .getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf ->
                        csrf.disable()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(
                                        authenticationEntryPoint
                                )
                                .accessDeniedHandler(
                                        accessDeniedHandler
                                )
                )

                .authenticationProvider(
                        authenticationProvider()
                )

                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/api/auth/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/actuator/health"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/resources/**"
                                )
                                .hasAnyRole(
                                        "RESPONDER",
                                        "DISPATCHER",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        "/api/assignments/**"
                                )
                                .hasAnyRole(
                                        "DISPATCHER",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        "/api/responders/**"
                                )
                                .hasAnyRole(
                                        "DISPATCHER",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        "/api/incidents/**"
                                )
                                .authenticated()

                                .anyRequest()
                                .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}