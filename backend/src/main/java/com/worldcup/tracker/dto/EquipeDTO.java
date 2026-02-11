package com.worldcup.tracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipeDTO {
    
    private Long id;
    
    private String nom;
    
    private String codePays;
    
    private String drapeauUrl;
    
    private String groupeCode;
    
    private Integer fifaRanking;
    
    private String confederation;
    
    // Group information as nested object
    private GroupeBasicDTO groupe;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GroupeBasicDTO {
        private Long id;
        private String lettre;
        private String nom;
    }
}