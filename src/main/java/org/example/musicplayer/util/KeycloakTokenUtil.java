package org.example.musicplayer.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for handling Keycloak tokens
 */
@UtilityClass
@Slf4j
public class KeycloakTokenUtil {

    /**
     * Gets the current user's ID from the security context
     *
     * @return User ID or null if not found
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            return jwtToken.getName();
        }
        return null;
    }

    /**
     * Gets the current user's claims from the security context
     *
     * @return Map of claims or empty map if not found
     */
    public Map<String, Object> getCurrentUserClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return jwt.getClaims();
        }
        return Collections.emptyMap();
    }

    /**
     * Gets the current user's roles from the security context
     *
     * @return Set of roles or empty set if not found
     */
    public Set<String> getCurrentUserRoles() {
        Map<String, Object> claims = getCurrentUserClaims();
        if (claims.containsKey("realm_access")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            if (realmAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                Set<String> roles = ((java.util.List<String>) realmAccess.get("roles"))
                        .stream()
                        .collect(Collectors.toSet());
                return roles;
            }
        }
        return Collections.emptySet();
    }

    /**
     * Checks if the current user has a specific role
     *
     * @param role Role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role);
    }

    /**
     * Gets a claim from the current user's token
     *
     * @param claimName Name of the claim
     * @return Claim value or null if not found
     */
    public Object getClaim(String claimName) {
        Map<String, Object> claims = getCurrentUserClaims();
        return claims.getOrDefault(claimName, null);
    }

    /**
     * Gets a token for embedding in Kafka messages (for service-to-service auth)
     *
     * @param token The current user's token
     * @return Token string for embedding in Kafka messages
     */
    public String getTokenForKafka(String token) {
        // Simply return the token for now, could add more processing if needed
        return token;
    }
} 