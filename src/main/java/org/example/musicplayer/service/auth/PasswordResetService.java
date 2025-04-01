package org.example.musicplayer.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.config.constraint.CrossOrigin;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.auth.request.PasswordResetCompleteRequest;
import org.example.musicplayer.dtos.auth.request.PasswordResetRequest;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.service.mail.MailService;
import org.example.musicplayer.util.WebUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;


@Service
@Slf4j
public class PasswordResetService {

    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CrossOrigin crossOrigin;

    public PasswordResetService(final MailService mailService,
                                final PasswordEncoder passwordEncoder,
                                final UserRepository userRepository,
                                final CrossOrigin crossOrigin) {
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.crossOrigin = crossOrigin;
    }

    private boolean hasValidRequest(final User user) {
        return user != null &&
                user.getPasswordResetKey() != null &&
                user.getPasswordResetDate() != null &&
                user.getPasswordResetDate().plusWeeks(1).isAfter(OffsetDateTime.now());
    }

    public String startProcess(final PasswordResetRequest passwordResetRequest) {
        log.info("received password reset request for {}", passwordResetRequest.getEmail());

        final User user = userRepository.findByEmailIgnoreCase(passwordResetRequest.getEmail());
        if (user == null) {
            log.warn("user {} not found", passwordResetRequest.getEmail());
            throw new NotFoundException(new ErrorDto("404", "This user was not found"));
        }

        if (!hasValidRequest(user)) {
            user.setPasswordResetKey(UUID.randomUUID());
        }
        user.setPasswordResetDate(OffsetDateTime.now());
        userRepository.save(user);

        String resetPasswordUrl = String.format("%s/reset-password/%s",
                crossOrigin.getAuthenticationUrl(),
                user.getPasswordResetKey());

        mailService.sendMail(
                passwordResetRequest.getEmail(),
                WebUtils.getMessage("Forgot password?"),
                WebUtils.renderTemplate("/mails/passwordReset", Collections.singletonMap("confirmation_url", resetPasswordUrl))
        );
        return "";
    }

    public boolean isValidPasswordResetUid(final UUID passwordResetUid) {
        final User user = userRepository.findByPasswordResetKey(passwordResetUid);
        if (hasValidRequest(user) && !isPasswordResetTokenExpired(user)) {
            return true;
        }
        log.warn("invalid or expired password reset uid {}", passwordResetUid);
        return false;
    }

    private boolean isPasswordResetTokenExpired(User user) {
        return user.getPasswordResetDate().isBefore(OffsetDateTime.now().minusHours(24));
    }

    public void completeProcess(final PasswordResetCompleteRequest passwordResetCompleteRequest) {
        log.info("Password reset complete request for {} , new password {}", passwordResetCompleteRequest.getUid(), passwordResetCompleteRequest.getNewPassword());
        final User user = userRepository.findByPasswordResetKey(passwordResetCompleteRequest.getUid());
        Assert.isTrue(hasValidRequest(user) && !isPasswordResetTokenExpired(user), "invalid or expired update password request");

        log.warn("updating password for user {}", user.getEmail());

        user.setPassword(passwordEncoder.encode(passwordResetCompleteRequest.getNewPassword()));
        user.setPasswordResetKey(null);
        user.setPasswordResetDate(null);
        userRepository.save(user);
    }

}
