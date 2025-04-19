package org.example.musicplayer.controller.auth;


import org.springframework.ui.Model;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.musicplayer.dtos.auth.request.AuthenticationRequest;
import org.example.musicplayer.dtos.auth.request.CompleteAuthenticationRequest;
import org.example.musicplayer.dtos.auth.request.RefreshTokenRequest;
import org.example.musicplayer.dtos.auth.response.AuthenticationResponse;
import org.example.musicplayer.dtos.auth.response.CompleteAuthenticationResponse;
import org.example.musicplayer.service.auth.RegistrationService;
import org.example.musicplayer.service.security.JwtRefreshTokenService;
import org.example.musicplayer.service.security.JwtTokenService;
import org.example.musicplayer.service.security.JwtUserDetails;
import org.example.musicplayer.service.security.JwtUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.example.musicplayer.config.constraint.Endpoints.API;


@RequestMapping(API+ "/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API endpoints for user authentication and registration management")
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final RegistrationService registrationService;

    @Operation(summary = "Authenticate user",
            description = "Authenticates a user with their email and password and returns JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @Parameter(description = "User credentials", required = true)
            @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        authenticationResponse.setRefreshToken(jwtRefreshTokenService.generateRefreshToken(userDetails.getId()));
        return authenticationResponse;
    }


    @Operation(summary = "Refresh token",
            description = "Generate new access token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New access token generated successfully",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/refresh")
    public AuthenticationResponse refresh(
            @Parameter(description = "Refresh token request", required = true)
            @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest) throws IllegalAccessException {
        final String username = jwtRefreshTokenService.validateRefreshTokenAndGetUsername(refreshTokenRequest.getRefreshToken());
        final JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        authenticationResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
        return authenticationResponse;
    }


    @Operation(summary = "Complete user registration",
            description = "Completes the registration process for a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration completed successfully",
                    content = @Content(schema = @Schema(implementation = CompleteAuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or registration data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/complete")
    public CompleteAuthenticationResponse completeRegistration(
            @Parameter(description = "Registration completion data", required = true)
            @RequestBody @Valid final CompleteAuthenticationRequest completeAuthenticationRequest) {
        return registrationService.completeRegistration(completeAuthenticationRequest);
    }
}