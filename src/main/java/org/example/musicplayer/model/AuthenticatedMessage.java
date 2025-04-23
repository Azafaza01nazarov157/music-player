package org.example.musicplayer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for messages sent to Kafka that include authentication information
 * This allows receiving services to verify the sender's identity and permissions
 * 
 * @param <T> The type of the payload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedMessage<T> {
    
    /**
     * The payload data
     */
    private T payload;
    
    /**
     * The JWT token of the authenticated user or service
     */
    private String jwtToken;
} 