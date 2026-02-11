package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "statistique_match",
       uniqueConstraints = @UniqueConstraint(name = "uk_stat_match", columnNames = "match_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatistiqueMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(precision = 5, scale = 2)
    private BigDecimal possessionEquipe1;

    @Column(precision = 5, scale = 2)
    private BigDecimal possessionEquipe2;

    private Integer tirsEquipe1;
    private Integer tirsEquipe2;

    private Integer tirsCadresEquipe1;
    private Integer tirsCadresEquipe2;

    private Integer cornersEquipe1;
    private Integer cornersEquipe2;

    private Integer fautesEquipe1;
    private Integer fautesEquipe2;

    private Integer cartonsJaunesEquipe1;
    private Integer cartonsJaunesEquipe2;

    private Integer cartonsRougesEquipe1;
    private Integer cartonsRougesEquipe2;

    private Integer horsJeuEquipe1;
    private Integer horsJeuEquipe2;
}