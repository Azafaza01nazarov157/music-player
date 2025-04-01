package org.example.musicplayer.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.musicplayer.dtos.auth.request.PasswordResetCompleteRequest;
import org.example.musicplayer.dtos.auth.request.PasswordResetRequest;
import org.example.musicplayer.service.auth.PasswordResetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.example.musicplayer.config.constraint.Endpoints.API;


@RestController
@RequestMapping(value = API + "/passwordReset", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Password Reset", description = "API endpoints for password reset functionality")
public class PasswordResetResource {

    private final PasswordResetService passwordResetService;

    public PasswordResetResource(final PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @Operation(
            summary = "Start password reset process",
            description = "Initiates the password reset process by sending a reset link to the user's email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset process started successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestBody @Valid final PasswordResetRequest passwordResetRequest) {
        passwordResetService.startProcess(passwordResetRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Validate password reset UID",
            description = "Checks if the provided password reset UID is valid and can be used to reset the password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "UID validation result",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid UID format"),
                    @ApiResponse(responseCode = "404", description = "UID not found or expired")
            }
    )
    @GetMapping("/isValidUid")
    public ResponseEntity<Boolean> isValidUid(@RequestParam("uid") UUID passwordResetUid) {
        boolean isValid = passwordResetService.isValidPasswordResetUid(passwordResetUid);
        return ResponseEntity.ok(isValid);
    }

    @Operation(
            summary = "Complete password reset process",
            description = "Completes the password reset process by setting a new password for the user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset completed successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "UID not found or expired"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Password reset already completed")
            }
    )
    @PostMapping("/complete")
    public ResponseEntity<Void> complete(@RequestBody @Valid final PasswordResetCompleteRequest passwordResetCompleteRequest) {
        passwordResetService.completeProcess(passwordResetCompleteRequest);
        return ResponseEntity.ok().build();
    }
}