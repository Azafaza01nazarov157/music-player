package org.example.musicplayer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "albums")
public class Album extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "cover_url")
    private String coverUrl;

    private String genre;

    private String description;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Track> tracks = new HashSet<>();
}