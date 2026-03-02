package com.worldcup.tracker.dto;

<<<<<<< HEAD
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
>>>>>>> 9a381468b38f76c71854414351345a9d0529e825
public class MatchDTO {
    
    private Long id;
<<<<<<< HEAD
    
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
=======
    private String phase;
    private TeamDTO teamA;
    private TeamDTO teamB;
    private String date;
    private String time;
    private String stadium;
    private String city;
    private String status;
    private Integer currentMinute;
    private List<MatchEventDTO> events;
}
>>>>>>> 9a381468b38f76c71854414351345a9d0529e825
