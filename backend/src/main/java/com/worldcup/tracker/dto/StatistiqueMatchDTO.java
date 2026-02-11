package com.worldcup.tracker.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatistiqueMatchDTO {
    
    private Long id;
    
    private Long matchId;
    
    private BigDecimal possessionEquipe1;
    
    private BigDecimal possessionEquipe2;
    
    private Integer tirsEquipe1;
    
    private Integer tirsEquipe2;
    
    private Integer tirsCadresEquipe1;
    
    private Integer tirsCadresEquipe2;
    
    private Integer cornersEquipe1;
    
    private Integer cornersEquipe2;
    
    private Integer fautesEquipe1;
    
    private Integer fautesEquipe2;
    
    private Integer cartonsJaunesEquipe1;
    
    private Integer cartonsJaunesEquipe2;
    
    private Integer cartonsRougesEquipe1;
    
    private Integer cartonsRougesEquipe2;
    
    private Integer horsJeuEquipe1;
    
    private Integer horsJeuEquipe2;
}