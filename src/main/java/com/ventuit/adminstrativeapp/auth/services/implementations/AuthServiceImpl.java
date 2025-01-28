package com.ventuit.adminstrativeapp.auth.services.implementations;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ventuit.adminstrativeapp.auth.dto.LoginDto;
import com.ventuit.adminstrativeapp.auth.dto.LogoutDto;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthClientErrorException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthServerErrorException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthUnauthorizedException;
import com.ventuit.adminstrativeapp.auth.services.interfaces.AuthServiceInterface;
import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;
import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;
import com.ventuit.adminstrativeapp.shared.validators.ObjectsValidator;

@Service
public class AuthServiceImpl implements AuthServiceInterface {

    @Autowired
    private KeycloakProvider keycloak;
    @Autowired
    private ObjectsValidator<LoginDto> loginDtoValidator;
    @Autowired
    private ObjectsValidator<LogoutDto> logoutDtoValidator;

    @Override
    public ResponseEntity<Map<String, String>> login(LoginDto login) {
        try {
            // Validating the login dto
            this.loginDtoValidator.validate(login);

            String tokenUrl = keycloak.getServerUrl() + "/realms/" + keycloak.getGestifySolutionRealmName()
                    + "/protocol/openid-connect/token";

            RestTemplate restTemplate = new RestTemplate();

            // Set the headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Set the form parameters
            MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
            formParams.add("client_id", keycloak.getGestifySolutionClientId());
            formParams.add("grant_type", "password");
            formParams.add("username", login.getUsername());
            formParams.add("password", login.getPassword());

            // Create the HttpEntity with the headers and form parameters
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, headers);

            // Make the POST request
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });

            return response;
        } catch (ObjectNotValidException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthUnauthorizedException("Invalid username or password");
            } else {
                throw new AuthClientErrorException(e.getMessage(), e.getStatusCode().value());
            }
        } catch (HttpServerErrorException e) {
            throw new AuthServerErrorException(e.getMessage(), e.getStatusCode().value());
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public ResponseEntity<Map<String, String>> logout(LogoutDto logout) {
        try {
            // Validating the logout dto
            this.logoutDtoValidator.validate(logout);

            String logoutUrl = keycloak.getServerUrl() + "/realms/" + keycloak.getGestifySolutionRealmName()
                    + "/protocol/openid-connect/logout";

            RestTemplate restTemplate = new RestTemplate();

            // Set the headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Set the form parameters
            MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
            formParams.add("client_id", keycloak.getGestifySolutionClientId());
            formParams.add("refresh_token", logout.getRefreshToken());

            // Create the HttpEntity with the headers and form parameters
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, headers);

            // Make the POST request
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    logoutUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });

            return response;
        } catch (ObjectNotValidException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            throw new AuthClientErrorException(e.getMessage(), e.getStatusCode().value());
        } catch (HttpServerErrorException e) {
            throw new AuthServerErrorException(e.getMessage(), e.getStatusCode().value());
        } catch (Exception e) {
            throw e;
        }
    }

}
