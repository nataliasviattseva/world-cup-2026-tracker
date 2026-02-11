package com.worldcup.tracker.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDTO {
    
    private Long id;
    
    private PhaseCompetitionDTO phase;
    
    private LocalDateTime dateHeure;
    
    private StadeDTO stade;
    
    private EquipeDTO equipe1;
    
    private EquipeDTO equipe2;
    
    private Integer scoreEquipe1;
    
    private Integer scoreEquipe2;
    
    private StatutMatchDTO statut;
    
    private String groupe;
    
    private Integer tempsReglementaire;
    
    private Boolean prolongations;
    
    private Boolean tirsAuBut;
    
    // Optional statistics
    private StatistiqueMatchDTO statistique;
    
    // Match events
    @Builder.Default
    private List<EvenementMatchDTO> evenements = new ArrayList<>();
    
    public enum StatutMatchDTO {
        A_VENIR,
        EN_COURS,
        TERMINE,
        REPORTE,
        ANNULE
    }
}