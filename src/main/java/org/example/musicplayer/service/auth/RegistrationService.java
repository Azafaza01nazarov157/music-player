package org.example.musicplayer.service.auth;


import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.config.constraint.CrossOrigin;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RoleRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.auth.request.CompleteAuthenticationRequest;
import org.example.musicplayer.dtos.auth.request.RegistrationRequest;
import org.example.musicplayer.dtos.auth.response.CompleteAuthenticationResponse;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.BadRequestException;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.service.mail.MailService;
import org.example.musicplayer.util.UserRoles;
import org.example.musicplayer.util.WebUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;


@Service
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CrossOrigin crossOrigin;
    private final MailService mailService;

    public RegistrationService(final UserRepository userRepository,
                               final PasswordEncoder passwordEncoder,
                               final RoleRepository roleRepository,
                               CrossOrigin crossOrigin,
                               MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.crossOrigin = crossOrigin;
        this.mailService = mailService;
    }

    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getEmail());

        UUID passwordResetKey = UUID.randomUUID();

        final User user = new User();
        user.setAuthentication(Boolean.FALSE);
        user.setAuthenticationKey(passwordResetKey);
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findTopByName(UserRoles.USER)));
        userRepository.save(user);
        String url = String.format("%s/complete-registration/%s",
                crossOrigin.getAuthenticationUrl(), passwordResetKey);

        log.info("url = {} ", url);

        mailService.sendMail(user.getEmail(),
                "Complete the registration",
                WebUtils.renderTemplate("/mails/authentication",
                        Collections.singletonMap("confirmation_url",
                                url)));
    }

    public CompleteAuthenticationResponse completeRegistration(final CompleteAuthenticationRequest completeAuthenticationRequest) {
        if (!checkCode(completeAuthenticationRequest)) {
            throw new BadRequestException(new ErrorDto("404", "Incorrect data: UUID and/or email are missing"));
        }

        final User user = userRepository.findByAuthenticationKey(UUID.fromString(completeAuthenticationRequest.getUuid()));
        if (user == null) {
            throw new NotFoundException(new ErrorDto("404", "Not found user"));
        }
        user.setAuthentication(Boolean.TRUE);
        userRepository.save(user);

        return CompleteAuthenticationResponse.builder()
                .code("SUCCESS")
                .email(user.getEmail())
                .build();
    }


    private boolean checkCode(final CompleteAuthenticationRequest completeAuthenticationRequest) {
        return completeAuthenticationRequest.getUuid() != null;
    }
}
