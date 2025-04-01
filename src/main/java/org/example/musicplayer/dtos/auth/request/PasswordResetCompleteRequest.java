package org.example.musicplayer.dtos.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordResetCompleteRequest {

    @NotNull
    @JsonProperty("uuid")
    private UUID uid;

    @NotNull
    @Size(max = 255)
    @JsonProperty("newPassword")
    private String newPassword;

}
