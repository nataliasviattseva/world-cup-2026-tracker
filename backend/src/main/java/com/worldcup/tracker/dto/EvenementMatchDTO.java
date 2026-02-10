package com.worldcup.tracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvenementMatchDTO {
    
    private Long id;
    
    private Long matchId;
    
    // Simplified team info for event
    private EquipeBasicDTO equipe;
    
    private String typeEvenement;
    
    private Integer minute;
    
    private Integer minuteAdditionnelle;
    
    private String joueurNom;
    
    private Integer joueurNumero;
    
    private String description;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EquipeBasicDTO {
        private Long id;
        private String nom;
        private String codePays;
    }
}