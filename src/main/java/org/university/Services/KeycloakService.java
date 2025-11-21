//This service handles ALL Keycloak operations (create, update, delete users).

package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;

@ApplicationScoped
public class KeycloakService {
    // Inject configuration from application.properties
    @ConfigProperty(name = "keycloak.admin.server-url")
    String serverUrl;

    @ConfigProperty(name = "keycloak.admin.realm")
    String realm;

    @ConfigProperty(name = "keycloak.admin.client-id")
    String clientId;

    @ConfigProperty(name = "keycloak.admin.username")
    String username;

    @ConfigProperty(name = "keycloak.admin.password")
    String password;

    @ConfigProperty(name = "keycloak.default.password")
    String defaultPassword;

    /**
     * Creates a Keycloak admin client for making API calls
     */
    private Keycloak getKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")  // Authenticate against master realm
                .clientId(clientId)
                .grantType("password")                 // REQUIRED
                .username(username)
                .password(password)
                .build();
    }

    /**
     * Creates a new user in Keycloak
     *
     * @param email User's email (used as username)
     * @param firstName User's first name
     * @param lastName User's last name
     * @param role Role to assign (admin/student/lecturer)
     * @return Keycloak User ID (UUID)
     */
    public String createUser(String email, String firstName, String lastName, String role) {
        Keycloak keycloak = getKeycloakClient();

        try {
            // Get the realm where we want to create the user
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user representation (the user data)
            UserRepresentation user = new UserRepresentation();
            user.setUsername(email);           // Username = email
            user.setEmail(email);              // Email address
            user.setFirstName(firstName);      // First name
            user.setLastName(lastName);        // Last name
            user.setEnabled(true);             // User is active
            user.setEmailVerified(true);       // Skip email verification

            // Create the user in Keycloak
            Response response = usersResource.create(user);


            // Check if creation was successful
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
            }

            // Extract user ID from the Location header
            String locationHeader = response.getHeaderString("Location");
            String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

            // Set user's password
            setUserPassword(usersResource, userId, defaultPassword);

            // Assign role to user
            assignRole(realmResource, userId, role);

            // Clean up
            response.close();
            keycloak.close();

            return userId;  // Return Keycloak ID

        } catch (Exception e) {
            keycloak.close();
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Sets password for a user
     */
    private void setUserPassword(UsersResource usersResource, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);  // Password doesn't need to be changed on first login

        usersResource.get(userId).resetPassword(credential);
    }

    /**
     * Assigns a role to a user
     */
    private void assignRole(RealmResource realmResource, String userId, String roleName) {
        try {

            // Get the role
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();

            // Assign role to user
            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(role));


        } catch (Exception e) {
            throw new RuntimeException("Failed to assign role '" + roleName + "'. Make sure the role exists in Keycloak!", e);
        }
    }

    /**
     * Updates user information in Keycloak
     */
    public void updateUser(String keycloakId, String email, String firstName, String lastName) {
        Keycloak keycloak = getKeycloakClient();

        try {
            RealmResource realmResource = keycloak.realm(realm);

            // Get existing user
            UserRepresentation user = realmResource.users().get(keycloakId).toRepresentation();

            // Update fields
            user.setEmail(email);
            user.setUsername(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            // Save changes
            realmResource.users().get(keycloakId).update(user);

            keycloak.close();
        } catch (Exception e) {
            keycloak.close();
            throw new RuntimeException("Error updating user in Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Disables user in Keycloak (soft delete - user cannot login)
     */
    public void disableUser(String keycloakId) {
        Keycloak keycloak = getKeycloakClient();

        try {
            RealmResource realmResource = keycloak.realm(realm);

            // Get user
            UserRepresentation user = realmResource.users().get(keycloakId).toRepresentation();

            // Disable user
            user.setEnabled(false);

            // Save changes
            realmResource.users().get(keycloakId).update(user);

            keycloak.close();
        } catch (Exception e) {
            keycloak.close();
            throw new RuntimeException("Error disabling user in Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Enables user in Keycloak (reactivate user)
     */
    public void enableUser(String keycloakId) {
        Keycloak keycloak = getKeycloakClient();

        try {
            RealmResource realmResource = keycloak.realm(realm);

            UserRepresentation user = realmResource.users().get(keycloakId).toRepresentation();
            user.setEnabled(true);

            realmResource.users().get(keycloakId).update(user);

            keycloak.close();
        } catch (Exception e) {
            keycloak.close();
            throw new RuntimeException("Error enabling user in Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Permanently deletes user from Keycloak (hard delete)
     * Use this carefully! Usually disable is better.
     */
    public void deleteUser(String keycloakId) {
        Keycloak keycloak = getKeycloakClient();

        try {
            RealmResource realmResource = keycloak.realm(realm);
            realmResource.users().get(keycloakId).remove();

            keycloak.close();
        } catch (Exception e) {
            keycloak.close();
            throw new RuntimeException("Error deleting user from Keycloak: " + e.getMessage(), e);
        }
    }

}
