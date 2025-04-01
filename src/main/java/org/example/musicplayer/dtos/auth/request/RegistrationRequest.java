package org.example.musicplayer.dtos.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.musicplayer.service.user.UserEmailUnique;
import org.example.musicplayer.util.WebUtils;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    @Email(regexp = WebUtils.EMAIL_PATTERN)
    @UserEmailUnique(message = "{registration.register.taken}")
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 72)
    private String password;

}
