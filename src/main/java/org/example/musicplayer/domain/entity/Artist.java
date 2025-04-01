package org.example.musicplayer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artists")
public class Artist extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String biography;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Album> albums = new HashSet<>();

    @OneToMany(mappedBy = "artist")
    @ToString.Exclude
    private Set<Track> tracks = new HashSet<>();
}