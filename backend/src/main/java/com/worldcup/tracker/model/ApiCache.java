package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_cache", indexes = {
        @Index(name = "idx_cache_url", columnList = "url"),
        @Index(name = "idx_cache_time", columnList = "cached_at")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String url;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String jsonResponse;

    @Column(name = "cached_at", nullable = false)
    private LocalDateTime cachedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        if (cachedAt == null) {
            cachedAt = LocalDateTime.now();
        }
    }
}
