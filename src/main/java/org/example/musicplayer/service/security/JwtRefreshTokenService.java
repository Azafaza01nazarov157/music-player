package org.example.musicplayer.service.security;


import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.RefreshToken;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RefreshTokenRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtRefreshTokenService {

    private static final Duration REFRESH_TOKEN_VALIDITY = Duration.ofHours(24);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtRefreshTokenService(final UserRepository userRepository,
            final RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateRefreshToken(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow();
        final String newToken = UUID.randomUUID().toString();
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(newToken);
        refreshToken.setExpirationTime(OffsetDateTime.now().plus(REFRESH_TOKEN_VALIDITY));
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        return newToken;
    }

    public String validateRefreshTokenAndGetUsername(final String givenToken) throws IllegalAccessException {
        final RefreshToken refreshToken = refreshTokenRepository.findByTokenAndExpirationTimeAfter(givenToken, OffsetDateTime.now());
        if (refreshToken == null) {
            log.warn("refresh token invalid");
            throw new IllegalAccessException();
        }
        return refreshToken.getUser().getEmail();
    }

}
