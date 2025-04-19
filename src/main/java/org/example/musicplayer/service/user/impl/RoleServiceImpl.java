package org.example.musicplayer.service.user.impl;

import jakarta.transaction.Transactional;
import java.util.List;

import org.example.musicplayer.domain.entity.Role;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RoleRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.user.RoleDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.mapper.RoleMapper;
import org.example.musicplayer.service.user.RoleService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    public RoleServiceImpl(final RoleRepository roleRepository, final RoleMapper roleMapper,
            final UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<RoleDTO> findAll() {
        final List<Role> roles = roleRepository.findAll(Sort.by("id"));
        return roles.stream()
                .map(role -> roleMapper.updateRoleDTO(role, new RoleDTO()))
                .toList();
    }


    public void updateArtistRole(User user) {
        user.setRoles(roleRepository.findByName("ARTIST"));
        userRepository.save(user);
    }

    @Override
    public RoleDTO get(final Long id) {
        return roleRepository.findById(id)
                .map(role -> roleMapper.updateRoleDTO(role, new RoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final RoleDTO roleDTO) {
        final Role role = new Role();
        roleMapper.updateRole(roleDTO, role);
        return roleRepository.save(role).getId();
    }

    @Override
    public void update(final Long id, final RoleDTO roleDTO) {
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roleMapper.updateRole(roleDTO, role);
        roleRepository.save(role);
    }

    @Override
    public void delete(final Long id) {
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByRoles(role)
                .forEach(user -> user.getRoles().remove(role));
        roleRepository.delete(role);
    }

}
