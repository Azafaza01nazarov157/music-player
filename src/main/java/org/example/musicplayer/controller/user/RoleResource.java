package org.example.musicplayer.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.dtos.user.RoleDTO;
import org.example.musicplayer.service.user.RoleService;
import org.example.musicplayer.service.user.UserService;
import org.example.musicplayer.util.UserRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.USER + "', '" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
@SecurityRequirement(name = "bearer-jwt")
public class RoleResource {

    private final RoleService roleService;
    private final UserService userService;

    public RoleResource(final RoleService roleService,
                        final UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Operation(
            summary = "Get all roles",
            description = "Retrieve a list of all available roles in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of roles"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @Operation(
            summary = "Get role by ID",
            description = "Retrieve a specific role by its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @Operation(
            summary = "Create a new role",
            description = "Add a new role to the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Role successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRole(@RequestBody @Valid final RoleDTO roleDTO) {
        final Long createdId = roleService.create(roleDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an existing role",
            description = "Update the details of an existing role by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRole(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(id, roleDTO);
        return ResponseEntity.ok(id);
    }

    @Operation(
            summary = "Delete a role",
            description = "Delete a role from the system by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Role successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/artist")
    @PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
    public ResponseEntity<Void> updateUserToArtist(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        roleService.updateArtistRole(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/make-artist")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Change user role to ARTIST (Admin only)",
            security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponse(responseCode = "200", description = "User role updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized request")
    @ApiResponse(responseCode = "403", description = "Forbidden access")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> makeUserArtist(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        roleService.updateArtistRole(user);
        return ResponseEntity.ok().build();
    }

}
