package com.ventuit.adminstrativeapp.keycloak;

import java.util.List;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakClientNotFoundException;

import lombok.Getter;

@Getter
@Component
public class KeycloakProvider {

    @Value("${app.keycloak.username}")
    private String user;

    @Value("${app.keycloak.password}")
    private String password;

    @Value("${app.keycloak.base-url}")
    private String baseUrl;

    @Value("${app.keycloak.realm-name}")
    private String gestifySolutionRealmName;

    @Value("${app.keycloak.client-name}")
    private String gestifySolutionClientId;

    @Value("${server.port}")
    private String gestifySolutionServerPort;

    private final String realmMasterName = "master";
    private final String adminCliClientId = "admin-cli";
    private final String realmManagementClientId = "realm-management";
    private final int poolSize = 10;

    private Keycloak keycloak = null;

    public Keycloak getKeycloak() {
        if (this.keycloak == null) {
            String serverUrl = "http://" + this.baseUrl + ":" + this.gestifySolutionServerPort;
            this.keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(this.realmMasterName)
                    .clientId(this.adminCliClientId)
                    .username(this.user)
                    .password(this.password)
                    .grantType(OAuth2Constants.PASSWORD)
                    .resteasyClient(
                            new ResteasyClientBuilderImpl()
                                    .connectionPoolSize(this.poolSize)
                                    .build())
                    .build();

        }
        return this.keycloak;
    }

    public RealmResource getGestifySolutionRealm() {
        return this.keycloak.realm(gestifySolutionRealmName);
    }

    public String getClientIdRealmManagementClient() {
        List<ClientRepresentation> clients = getGestifySolutionRealm().clients().findAll();

        // Find the 'realm-management' client
        String realmManagementId = clients.stream()
                .filter(client -> realmManagementClientId.equals(client.getClientId()))
                .findFirst()
                .map(ClientRepresentation::getId)
                .orElseThrow(
                        () -> new KeycloakClientNotFoundException(realmManagementClientId, gestifySolutionRealmName));

        return realmManagementId;
    }

    public String getClientIdGestifySolutionClient() {
        String gestifySolutionClientId = getGestifySolutionRealm()
                .clients()
                .findByClientId(this.gestifySolutionClientId)
                .stream()
                .findFirst()
                .map(ClientRepresentation::getId)
                .orElseThrow(() -> new KeycloakClientNotFoundException(this.gestifySolutionClientId,
                        gestifySolutionRealmName));
        return gestifySolutionClientId;
    }

    public boolean isGestifySolutionRealmExist() {
        RealmsResource realmsResource = getKeycloak().realms();

        // Check if the realm already exists
        boolean realmExists = realmsResource.findAll().stream()
                .anyMatch(realm -> realm.getRealm().equals(gestifySolutionRealmName));

        return realmExists;
    }

    public boolean isGestifySolutionClientExist() {
        boolean clientExists = getGestifySolutionRealm().clients()
                .findByClientId(gestifySolutionClientId)
                .stream()
                .anyMatch(client -> client.getClientId().equals(gestifySolutionClientId));
        return clientExists;
    }

}
