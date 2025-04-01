package org.example.musicplayer.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.musicplayer.dtos.auth.request.RegistrationRequest;
import org.example.musicplayer.service.auth.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.musicplayer.config.constraint.Endpoints.API;


@RestController
@RequestMapping(API)
@Tag(name = "Registration", description = "API endpoints for user registration functionality")
public class RegistrationResource {

    private final RegistrationService registrationService;

    public RegistrationResource(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided details. Initiates the registration process.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Conflict - User already exists")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid final RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

}
