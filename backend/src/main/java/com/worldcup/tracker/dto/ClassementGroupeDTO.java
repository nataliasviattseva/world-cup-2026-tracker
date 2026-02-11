package com.worldcup.tracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassementGroupeDTO {
    
    private Long id;
    
    private GroupeDTO groupe;
    
    private EquipeDTO equipe;
    
    private Integer matchsJoues;
    
    private Integer victoires;
    
    private Integer nuls;
    
    private Integer defaites;
    
    private Integer butsPour;
    
    private Integer butsContre;
    
    private Integer differenceButs;
    
    private Integer points;
    
    private Integer position;
}