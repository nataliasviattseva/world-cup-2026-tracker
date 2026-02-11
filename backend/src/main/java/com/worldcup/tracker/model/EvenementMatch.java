package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evenement_match", indexes = {
        @Index(name = "idx_event_match", columnList = "match_id"),
        @Index(name = "idx_event_equipe", columnList = "equipe_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EvenementMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @Column(name = "type_evenement", length = 50, nullable = false)
    private String typeEvenement;

    private Integer minute;

    @Column(name = "minute_additionnelle")
    private Integer minuteAdditionnelle;

    @Column(name = "joueur_nom", length = 100)
    private String joueurNom;

    @Column(name = "joueur_numero")
    private Integer joueurNumero;

    @Column(columnDefinition = "TEXT")
    private String description;
}