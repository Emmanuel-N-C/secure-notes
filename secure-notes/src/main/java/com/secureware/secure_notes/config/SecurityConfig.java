package com.secureware.secure_notes.config;

import com.secureware.secure_notes.security.TokenAuthEntryPoint;
import com.secureware.secure_notes.security.TokenAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenAuthFilter tokenAuthFilter;
    private final TokenAuthEntryPoint tokenAuthEntryPoint;

    // Explicit constructor instead of @RequiredArgsConstructor
    public SecurityConfig(TokenAuthFilter tokenAuthFilter, TokenAuthEntryPoint tokenAuthEntryPoint) {
        this.tokenAuthFilter = tokenAuthFilter;
        this.tokenAuthEntryPoint = tokenAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health", "/h2-console/**").permitAll()
                        .requestMatchers("/notes/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(tokenAuthEntryPoint)
                )
                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // For H2 console

        return http.build();
    }
}