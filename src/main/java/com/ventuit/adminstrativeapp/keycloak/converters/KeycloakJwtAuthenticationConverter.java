package com.ventuit.adminstrativeapp.keycloak.converters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    @Nullable
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractRoles(source).stream())
                        .collect(Collectors.toSet()));
    }

    private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {
        // Extract the "resource_access" claim from the JWT
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        // Extract the "account" object from the "resource_access" claim
        Object accountObj = resourceAccess.get("account");
        if (!(accountObj instanceof Map)) {
            return Collections.emptySet();
        }

        Map<?, ?> accountMap = (Map<?, ?>) accountObj;
        Object rolesObj = accountMap.get("roles");
        if (!(rolesObj instanceof List)) {
            return Collections.emptySet();
        }

        List<?> rolesList = (List<?>) rolesObj;
        if (rolesList.isEmpty() || !(rolesList.get(0) instanceof String)) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) rolesList;

        // Convert roles to GrantedAuthority objects
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_").toUpperCase()))
                .collect(Collectors.toSet());
    }

}
