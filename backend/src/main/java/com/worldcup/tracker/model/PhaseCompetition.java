package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "phase_competition")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhaseCompetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private PhaseNomEnum nom;

    @Column(nullable = false)
    private Integer ordre;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer nombreMatchs;
}