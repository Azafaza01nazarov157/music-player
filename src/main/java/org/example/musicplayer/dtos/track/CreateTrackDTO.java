package org.example.musicplayer.dtos.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrackDTO {
    private String title;
    private Long albumId;
    private BigDecimal duration;
    private Integer trackNumber;
    private String genre;
    private Long playCount;
    private String status;
}
