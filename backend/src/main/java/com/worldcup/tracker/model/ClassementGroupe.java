package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classement_groupe",
       uniqueConstraints = @UniqueConstraint(name = "uk_classement_groupe_equipe", columnNames = {"groupe_id", "equipe_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassementGroupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupe_id", nullable = false)
    private Groupe groupe;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;

    private Integer matchsJoues;
    private Integer victoires;
    private Integer nuls;
    private Integer defaites;

    private Integer butsPour;
    private Integer butsContre;
    private Integer differenceButs;

    private Integer points;
    private Integer position;
}