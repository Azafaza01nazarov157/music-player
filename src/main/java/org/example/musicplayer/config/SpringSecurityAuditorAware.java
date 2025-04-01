package org.example.musicplayer.config;


import org.example.musicplayer.config.constraint.Constants;
import org.example.musicplayer.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of Spring Data JPA's {@link AuditorAware} interface for retrieving the current
 * auditor. In this implementation, it retrieves the current user login using {@link SecurityUtils}
 * and falls back to the system user if no user is authenticated.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    /**
     * Retrieves the current auditor (user login).
     *
     * @return An optional containing the current user login if available, or the system user
     * ("SYSTEM") as a fallback.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM));
    }
}
