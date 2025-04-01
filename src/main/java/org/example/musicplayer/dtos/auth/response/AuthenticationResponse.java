package org.example.musicplayer.dtos.auth.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;

}
