package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches", indexes = {
        @Index(name = "idx_match_phase", columnList = "phase_id"),
        @Index(name = "idx_match_date_heure", columnList = "date_heure"),
        @Index(name = "idx_match_statut", columnList = "statut")
})
@Check(constraints = "equipe1_id <> equipe2_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    private PhaseCompetition phase;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "stade_id", nullable = false)
    private Stade stade;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe1_id", nullable = false)
    private Equipe equipe1;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe2_id", nullable = false)
    private Equipe equipe2;

    @Column(name = "score_equipe1")
    private Integer scoreEquipe1;

    @Column(name = "score_equipe2")
    private Integer scoreEquipe2;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatutMatchEnum statut;

    @Column(name = "groupe", length = 10)
    private String groupe; // si tu gardes ce champ dans MATCH

    @Column(name = "temps_reglementaire")
    private Integer tempsReglementaire;

    private Boolean prolongations;
    @Column(name = "tirs_au_but")
    private Boolean tirsAuBut;

    // 0..1 statistiques
    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private StatistiqueMatch statistique;

    // 0..n événements
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EvenementMatch> evenements = new ArrayList<>();
}