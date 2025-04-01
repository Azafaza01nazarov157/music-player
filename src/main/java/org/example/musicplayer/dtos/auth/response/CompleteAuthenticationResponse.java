package org.example.musicplayer.dtos.auth.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompleteAuthenticationResponse {

    @NotNull
    @Size(max = 72)
    private String email;

    @NotNull
    @Size(max = 72)
    private String code;
}
