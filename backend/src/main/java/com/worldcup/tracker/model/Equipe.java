package com.worldcup.tracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipe", indexes = {
        @Index(name = "idx_equipe_code_pays", columnList = "code_pays")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nom;

    @Column(name = "code_pays", length = 3, nullable = false)
    private String codePays;

    @Column(name = "drapeau_url", length = 255)
    private String drapeauUrl;

    @Column(length = 10)
    private String groupeCode; // si tu gardes le champ "groupe" dans ta table EQUIPE

    private Integer fifaRanking;

    @Column(length = 50)
    private String confederation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupe_id")
    @JsonBackReference
    private Groupe groupe;
}