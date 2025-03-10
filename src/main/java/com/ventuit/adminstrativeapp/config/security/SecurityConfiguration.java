package com.ventuit.adminstrativeapp.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;
import com.ventuit.adminstrativeapp.keycloak.converters.KeycloakOpaqueTokenAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private KeycloakProvider keycloak;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String introspectionUri = keycloak.getBaseUrl() + "/realms/" + keycloak.getGestifySolutionRealmName()
                + "/protocol/openid-connect/token/introspect";
        String clientId = keycloak.getGestifySolutionAdminClientId();
        String clientSecret = keycloak.getCredentialsAdminClient();

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
                    auth.requestMatchers(HttpMethod.POST, "/auth/refresh-token").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/bosses").permitAll();
                    auth.requestMatchers("/docs/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(authResourceServer -> {
                    authResourceServer
                            .opaqueToken(token -> token
                                    .authenticationConverter(new KeycloakOpaqueTokenAuthenticationConverter())
                                    .introspectionUri(introspectionUri)
                                    .introspectionClientCredentials(clientId, clientSecret));
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .build();
    }

}
