package org.example.musicplayer.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.musicplayer.dtos.user.PasswordChangeDTO;
import org.example.musicplayer.dtos.user.UserDTO;
import org.example.musicplayer.exception.errors.ReferencedException;
import org.example.musicplayer.service.user.UserService;
import org.example.musicplayer.util.ReferencedWarning;
import org.example.musicplayer.util.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.example.musicplayer.config.constraint.Endpoints.API;


@RestController
@RequestMapping(value = API + "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.USER + "', '" + UserRoles.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a paginated list of all users with optional filtering.",
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "Page number to retrieve",
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "Number of items per page",
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            description = "Sorting criteria",
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authentication credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions to access users")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(filter, pageable));
    }

    @Operation(
            summary = "Get user information",
            description = "Returns user data by their ID if the request has the necessary access rights."
    )
    @ApiResponse(responseCode = "200", description = "User successfully found and returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized access – missing or invalid authentication credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions to view user data")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.get(principal.getName()));
    }

    @Operation(
            summary = "Create new user",
            description = "Create a new user with the provided details."
    )
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authentication credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions to create user")
    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Long createdId = userService.create(userDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user", description = "Update the authenticated user's information.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/me")
    public ResponseEntity<Long> updateUser(Principal principal, @RequestBody @Valid final UserDTO userDTO) {
        Long userId = Long.parseLong(principal.getName());
        userService.update(userId, userDTO);
        return ResponseEntity.ok(userId);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a specific user by their ID."
    )
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authentication credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions to delete user")
    @ApiResponse(responseCode = "409", description = "User cannot be deleted due to existing references")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change user password", description = "Change the authenticated user's password.")
    @ApiResponse(responseCode = "200", description = "Password successfully changed")
    @ApiResponse(responseCode = "400", description = "Invalid old password or validation failed")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(Principal principal, @RequestBody @Valid final PasswordChangeDTO passwordChangeDTO) {
        Long userId = Long.parseLong(principal.getName());
        userService.changePassword(userId, passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
