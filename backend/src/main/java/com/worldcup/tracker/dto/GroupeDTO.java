package com.worldcup.tracker.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupeDTO {
    
    private Long id;
    
    private String lettre;
    
    private String nom;
    
    @Builder.Default
    private List<EquipeDTO> equipes = new ArrayList<>();
}