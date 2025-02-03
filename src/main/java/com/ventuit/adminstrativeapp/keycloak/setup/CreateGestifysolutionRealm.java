package com.ventuit.adminstrativeapp.keycloak.setup;

import org.keycloak.representations.idm.RealmRepresentation;
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

    @Override
    public void run(String... args) throws Exception {
        this.createRealm();
    }

    public void createRealm() {
        if (keycloakProvider.isGestifySolutionRealmExist()) {
            System.out.println("Realm " + keycloakProvider.getGestifySolutionRealmName() + " already exists.");
            return; // Exit if the realm already exists
        }

        // Create the new realm if it does not exist
        RealmRepresentation newRealm = new RealmRepresentation();
        newRealm.setRealm(keycloakProvider.getGestifySolutionRealmName());
        newRealm.setEnabled(true);

        // Create the realm
        this.keycloakProvider.getKeycloak().realms().create(newRealm);
        System.out.println("Realm " + keycloakProvider.getGestifySolutionRealmName() + " created successfully.");
    }
}
