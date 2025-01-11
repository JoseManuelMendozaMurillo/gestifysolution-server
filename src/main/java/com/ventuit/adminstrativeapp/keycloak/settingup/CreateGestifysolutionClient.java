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
@Order(2)
public class CreateGestifysolutionClient implements CommandLineRunner {

    private final KeycloakProvider keycloakProvider;

    @Value("${app.keycloak.client-name}")
    private String clientName;

    @Value("${app.keycloak.realm-name}")
    private String realmName;

    @Value("${app.domain}")
    private String domain;

    @Value("${app.ssl}")
    private String ssl;

    @Value("${server.port}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        this.createClient();
    }

    private void createClient() {
        // Check if the client already exists
        boolean clientExists = keycloakProvider.getRealm().clients().findByClientId(clientName).stream()
                .anyMatch(client -> client.getClientId().equals(clientName));

        if (clientExists) {
            System.out.println("Client " + clientName + " already exists in realm " + realmName);
            return;
        }

        // Define the root url for the application
        String rootUrl;

        if (this.ssl.equals("true")) {
            rootUrl = "https://";
        } else {
            rootUrl = "http://";
        }
        rootUrl = rootUrl + this.domain + ":" + this.port;

        // Define the new client
        ClientRepresentation client = new ClientRepresentation();
        client.setProtocol("openid-connect");
        client.setClientId(clientName);
        client.setName(clientName);
        client.setDescription("Gestifysolution server application");
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
        keycloakProvider.getRealm().clients().create(client);
        System.out.println("Client " + clientName + " created successfully in realm " + realmName);
    }

}
