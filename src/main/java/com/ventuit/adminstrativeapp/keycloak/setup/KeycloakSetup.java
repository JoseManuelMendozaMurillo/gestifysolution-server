package com.ventuit.adminstrativeapp.keycloak.setup;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component("keycloakSetup")
@RequiredArgsConstructor
public class KeycloakSetup {
    private final CreateGestifysolutionRealm createRealm;
    private final CreateGestifysolutionAdminClient createAdminClient;
    private final CreateGestifysolutionClient createClient;

    @PostConstruct
    public void setup() {
        createRealm.createRealm();
        createAdminClient.createAdminClient();
        createClient.createClient();
    }
}