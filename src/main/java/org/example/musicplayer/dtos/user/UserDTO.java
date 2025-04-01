package org.example.musicplayer.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.example.musicplayer.service.user.UserEmailUnique;
import org.example.musicplayer.util.WebUtils;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @Email(regexp = WebUtils.EMAIL_PATTERN)
    @UserEmailUnique
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String password;

    private List<Long> roles;

}
