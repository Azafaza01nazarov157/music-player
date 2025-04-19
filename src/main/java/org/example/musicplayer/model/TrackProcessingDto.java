package org.example.musicplayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TrackProcessingDto {

    @JsonProperty("track_id")
    private String trackId;

    @JsonProperty("album_id")
    private String albumId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_format")
    private String fileFormat;
}
