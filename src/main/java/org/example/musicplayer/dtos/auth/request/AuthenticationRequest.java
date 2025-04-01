package org.example.musicplayer.dtos.auth.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.musicplayer.util.WebUtils;


@Getter
@Setter
public class AuthenticationRequest {

    @NotNull
    @Size(max = 255)
    @Email(regexp = WebUtils.EMAIL_PATTERN)
    private String email;

    @NotNull
    @Size(max = 72)
    private String password;

}
