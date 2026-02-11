package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "stade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Stade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String nom;

    @Column(length = 100)
    private String ville;

    @Column(length = 100)
    private String pays;

    private Integer capacite;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(length = 50)
    private String timezone;
}