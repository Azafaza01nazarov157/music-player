package org.example.musicplayer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    /**
     * Creates a Keycloak admin client
     * 
     * @return Keycloak admin client
     */
    private Keycloak getKeycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master") // Admin operations are always done on the master realm
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    /**
     * Creates a new user in Keycloak
     * 
     * @param username User's username
     * @param email User's email
     * @param firstName User's first name
     * @param lastName User's last name
     * @param password User's password
     * @param roles User's roles
     * @return User ID if the user was created successfully, null otherwise
     */
    public String createUser(String username, String email, String firstName, String lastName, 
                             String password, List<String> roles) {
        Keycloak keycloakAdmin = getKeycloakAdminClient();
        
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        
        // Get realm
        RealmResource realmResource = keycloakAdmin.realm(realm);
        UsersResource usersResource = realmResource.users();
        
        // Create user (requires id)
        Response response = usersResource.create(user);
        String userId = getCreatedId(response);
        
        if (userId == null) {
            return null;
        }
        
        // Set password
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        
        usersResource.get(userId).resetPassword(passwordCred);
        
        // Add roles
        for (String role : roles) {
            RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();
            realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        }
        
        return userId;
    }

    /**
     * Gets the service account access token for service-to-service communication
     * 
     * @return Access token
     */
    public String getServiceAccountToken() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
        
        return keycloak.tokenManager().getAccessTokenString();
    }
    
    /**
     * Gets the ID of a created user from the response
     * 
     * @param response Response from Keycloak
     * @return User ID
     */
    private String getCreatedId(Response response) {
        if (response.getStatus() != 201) {
            log.error("Failed to create user, status: {}", response.getStatus());
            return null;
        }
        
        String location = response.getHeaderString("Location");
        if (location == null) {
            return null;
        }
        
        String[] paths = location.split("/");
        return paths[paths.length - 1];
    }
} 