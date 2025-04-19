package org.example.musicplayer.dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted;
}

