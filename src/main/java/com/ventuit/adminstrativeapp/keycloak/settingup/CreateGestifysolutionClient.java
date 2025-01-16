package com.ventuit.adminstrativeapp.keycloak.settingup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Order(3)
public class CreateGestifysolutionClient implements CommandLineRunner {

    private final KeycloakProvider keycloakProvider;

    @Value("${app.domain}")
    private String domain;

    @Value("${app.ssl}")
    private String ssl;

    @Override
    public void run(String... args) throws Exception {
        this.createClient();
    }

    private void createClient() {
        if (keycloakProvider.isGestifySolutionClientExist()) {
            System.out.println("Client " + keycloakProvider.getGestifySolutionClientId() + " already exists in realm "
                    + keycloakProvider.getGestifySolutionRealmName());
            return;
        }

        // Define the root url for the application
        String rootUrl;
        if (this.ssl.equals("true")) {
            rootUrl = "https://";
        } else {
            rootUrl = "http://";
        }
        rootUrl = rootUrl + this.domain + ":" + keycloakProvider.getGestifySolutionServerPort();

        // Define the new client
        ClientRepresentation client = new ClientRepresentation();
        client.setProtocol("openid-connect");
        client.setClientId(keycloakProvider.getGestifySolutionClientId());
        client.setName(keycloakProvider.getGestifySolutionClientId());
        client.setDescription("Gestifysolution server application client");
        client.setStandardFlowEnabled(true);
        client.setDirectAccessGrantsEnabled(true);
        client.setRootUrl(rootUrl);
        client.setBaseUrl(rootUrl);
        client.setRedirectUris(Collections.singletonList(rootUrl + "/*"));
        client.setWebOrigins(Collections.singletonList(rootUrl));
        client.setEnabled(true);
        client.setPublicClient(true);

        // Set the Post Logout Redirect URIs
        Map<String, String> attributes = new HashMap<>();
        attributes.put("post.logout.redirect.uris", rootUrl);
        client.setAttributes(attributes);

        // Create the client
        keycloakProvider.getGestifySolutionRealm().clients().create(client);

        System.out.println("Client " + keycloakProvider.getGestifySolutionClientId() + " created successfully in realm "
                + keycloakProvider.getGestifySolutionRealmName());
    }

}
