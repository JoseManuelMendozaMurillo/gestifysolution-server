package com.ventuit.adminstrativeapp.shared.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

/**
 * A reusable component to extract information from the current security
 * context.
 */
@Component
public class AuthenticationHelper {

    private static final String DEFAULT_USERNAME = "Gestify solution server";

    /**
     * Retrieves the 'preferred_username' from the authenticated OAuth2 principal.
     *
     * @return The username of the authenticated user, or a default value if not
     *         found.
     */
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            // It's safer to check if the attribute exists before getting it
            if (principal.getAttributes().containsKey("preferred_username")) {
                return principal.getAttribute("preferred_username");
            }
        }
        return DEFAULT_USERNAME;
    }
}
