package org.example.musicplayer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_settings")
public class UserSettings extends AbstractAuditingEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "theme")
    private String theme = "light";

    @Column(name = "language")
    private String language = "en";

    @Column(name = "auto_play")
    private Boolean autoPlay = false;

    @Column(name = "preferred_quality")
    private String preferredQuality = "high";
}