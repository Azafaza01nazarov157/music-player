package org.example.musicplayer.dtos.auth.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RefreshTokenRequest {

    @NotNull
    @Size(max = 255)
    private String refreshToken;

}
