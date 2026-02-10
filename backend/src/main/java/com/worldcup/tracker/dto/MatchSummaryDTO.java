package com.worldcup.tracker.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Simplified MatchDTO for lists and summaries without full object graphs
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchSummaryDTO {
    
    private Long id;
    
    private String phaseName;
    
    private LocalDateTime dateHeure;
    
    private String stadeNom;
    
    private String stadeVille;
    
    private String equipe1Nom;
    
    private String equipe1CodePays;
    
    private String equipe2Nom;
    
    private String equipe2CodePays;
    
    private Integer scoreEquipe1;
    
    private Integer scoreEquipe2;
    
    private MatchDTO.StatutMatchDTO statut;
    
    private String groupe;
    
    private Boolean prolongations;
    
    private Boolean tirsAuBut;
}