package com.ventuit.adminstrativeapp.keycloak.settingup;

import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Order(1)
public class CreateGestifysolutionRealm implements CommandLineRunner {

    private final KeycloakProvider keycloakProvider;

    @Value("${app.keycloak.realm-name}")
    private String realmName;

    @Override
    public void run(String... args) throws Exception {
        this.createRealm();
    }

    public void createRealm() {
        // Get the Realms resource
        RealmsResource realmsResource = this.keycloakProvider.getKeycloak().realms();

        // Check if the realm already exists
        boolean realmExists = realmsResource.findAll().stream()
                .anyMatch(realm -> realm.getRealm().equals(realmName));

        if (realmExists) {
            System.out.println("Realm " + realmName + " already exists.");
            return; // Exit if the realm already exists
        }

        // Create the new realm if it does not exist
        RealmRepresentation newRealm = new RealmRepresentation();
        newRealm.setRealm(realmName);
        newRealm.setEnabled(true);

        // Create the realm
        realmsResource.create(newRealm);
        System.out.println("Realm " + realmName + " created successfully.");
    }
}
