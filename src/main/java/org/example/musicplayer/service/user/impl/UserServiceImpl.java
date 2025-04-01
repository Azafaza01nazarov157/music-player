package org.example.musicplayer.service.user.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.RefreshToken;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RefreshTokenRepository;
import org.example.musicplayer.domain.repository.RoleRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.user.UserDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.BadRequestException;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.mapper.UserMapper;
import org.example.musicplayer.service.user.UserService;
import org.example.musicplayer.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserServiceImpl(final UserRepository userRepository,
                           final RoleRepository roleRepository,
                           final PasswordEncoder passwordEncoder,
                           final UserMapper userMapper,
                           final RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Page<UserDTO> findAll(final String filter, final Pageable pageable) {
        Page<User> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                log.warn("Invalid filter value for ID parsing: {}", filter);
            }
            page = userRepository.findAllById(longFilter, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public UserDTO get(final String email) {
        return userRepository.findByEmail(email)
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final UserDTO userDTO) {
        final User user = new User();
        userMapper.updateUser(userDTO, user, roleRepository, passwordEncoder);
        return userRepository.save(user).getId();
    }

    @Override
    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userMapper.updateUser(userDTO, user, roleRepository, passwordEncoder);
        userRepository.save(user);
    }

    @Override
    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final RefreshToken userRefreshToken = refreshTokenRepository.findFirstByUser(user);
        if (userRefreshToken != null) {
            referencedWarning.setKey("user.refreshToken.user.referenced");
            referencedWarning.addParam(userRefreshToken.getId());
            return referencedWarning;
        }
        return null;
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException(new ErrorDto("404", "Current password is incorrect"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "User with email " + email + " not found")));
    }

    @Override
    public User getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "User with id " + id + " not found")));
    }

}
