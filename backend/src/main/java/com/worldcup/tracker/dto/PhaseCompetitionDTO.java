package com.worldcup.tracker.dto;

import lombok.*;

import java.time.LocalDate;

import com.worldcup.tracker.model.PhaseNomEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseCompetitionDTO {
    
    private Long id;
    
    private PhaseNomDTO nom;
    
    private Integer ordre;
    
    private LocalDate dateDebut;
    
    private LocalDate dateFin;
    
    private String description;
    
    private Integer nombreMatchs;
    
    public enum PhaseNomDTO {
        PHASE_GROUPES,
        SEIZIEMES_FINALE,
        HUITIEMES_FINALE,
        QUARTS_FINALE,
        DEMI_FINALES,
        PETITE_FINALE,
        FINALE
    }
}