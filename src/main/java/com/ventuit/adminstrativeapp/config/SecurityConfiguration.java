package com.ventuit.adminstrativeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.ventuit.adminstrativeapp.keycloak.converters.KeycloakJwtAuthenticationConverter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> {
                    csrf.disable();
                })
                .cors(cors -> {
                    cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/auth/logout").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/bosses").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(authResourceServer -> {
                    authResourceServer
                            .jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()));
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .build();
    }

}
