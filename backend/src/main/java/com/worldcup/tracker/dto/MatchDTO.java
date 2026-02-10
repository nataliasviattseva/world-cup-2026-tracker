package com.worldcup.tracker.dto;

import lombok.Data;

@Data
public class MatchDTO {
    private Long id;
    private String equipe1;
    private String equipe2;
    private Integer scoreEquipe1;
    private Integer scoreEquipe2;
    private String groupe;
    private String phase;
    private String statut;
    private String stade;
    private String dateHeure; // formaté pour le frontend
}
