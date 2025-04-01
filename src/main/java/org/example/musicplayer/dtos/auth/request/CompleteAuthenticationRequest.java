package org.example.musicplayer.dtos.auth.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompleteAuthenticationRequest {

    @NotNull
    @Size(max = 72)
    private String uuid;
}
