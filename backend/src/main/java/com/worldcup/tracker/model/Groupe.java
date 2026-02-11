package com.worldcup.tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groupe")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1, nullable = false)
    private String lettre;

    @Column(length = 50, nullable = false)
    private String nom;

    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Equipe> equipes = new ArrayList<>();
}