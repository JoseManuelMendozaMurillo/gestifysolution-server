package com.ventuit.adminstrativeapp.keycloak.converters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeycloakOpaqueTokenAuthenticationConverter implements OpaqueTokenAuthenticationConverter {

    private Collection<GrantedAuthority> extractAuthorities(Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> resourceAccess = (Map<String, Object>) attributes.get("resource_access");
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("account");
        if (clientAccess == null) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) clientAccess.get("roles");
        if (roles == null) {
            return Collections.emptySet();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }

    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        return new UsernamePasswordAuthenticationToken(
                authenticatedPrincipal,
                introspectedToken,
                extractAuthorities(authenticatedPrincipal.getAttributes()));
    }

}
