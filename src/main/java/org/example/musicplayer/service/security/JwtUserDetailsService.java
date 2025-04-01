package org.example.musicplayer.service.security;

import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByEmailIgnoreCase(username);
        if (user == null) {
            log.warn("User with email {} not found", username);
            throw new UsernameNotFoundException("User with email " + username + " not found");
        }

        if (!Boolean.TRUE.equals(user.getAuthentication())) {
            log.warn("User {} has not completed OTP verification", username);
            throw new UsernameNotFoundException("User " + username + " has not completed OTP verification");
        }
        final List<SimpleGrantedAuthority> authorities = user.getRoles() == null ? Collections.emptyList() :
                user.getRoles()
                        .stream()
                        .filter(roleRef -> roleRef.getName() != null)
                        .map(roleRef -> new SimpleGrantedAuthority(roleRef.getName()))
                        .toList();
        return new JwtUserDetails(user.getId(), username, user.getPassword(), authorities);
    }

}
