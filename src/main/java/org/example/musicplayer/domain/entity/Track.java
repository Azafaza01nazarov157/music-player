package org.example.musicplayer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tracks")
public class Track extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_format")
    private String fileFormat;

    @Column(name = "status")
    private String status;

    private BigDecimal duration;

    @Column(name = "bit_rate")
    private Integer bitRate;

    @Column(name = "sample_rate")
    private Integer sampleRate;

    private Integer track_number;

    private String genre;

    @Column(name = "play_count")
    private Long playCount = 0L;

    @ManyToMany(mappedBy = "tracks")
    @ToString.Exclude
    private Set<Playlist> playlists = new HashSet<>();

    @OneToMany(mappedBy = "track")
    @ToString.Exclude
    private Set<PlayHistory> playHistory = new HashSet<>();
}