package com.ventuit.adminstrativeapp.keycloak;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProvider {

    @Value("${app.keycloak.username}")
    private String user;

    @Value("${app.keycloak.password}")
    private String password;

    @Value("${app.keycloak.base-url}")
    private String baseUrl;

    private final String PORT = "8080";
    private final String REALM_NAME = "master";
    private final String CLIENT = "admin-cli";
    private final int POOL_SIZE = 10;

    private Keycloak keycloak = null;

    public Keycloak getKeycloak() {
        if (this.keycloak == null) {
            String serverUrl = "http://" + this.baseUrl + ":" + this.PORT;
            this.keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(this.REALM_NAME)
                    .clientId(this.CLIENT)
                    .username(this.user)
                    .password(this.password)
                    .grantType(OAuth2Constants.PASSWORD)
                    .resteasyClient(
                            new ResteasyClientBuilderImpl()
                                    .connectionPoolSize(this.POOL_SIZE)
                                    .build())
                    .build();

        }
        return this.keycloak;
    }

}
