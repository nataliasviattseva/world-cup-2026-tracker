package com.worldcup.tracker.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StadeDTO {
    
    private Long id;
    
    private String nom;
    
    private String ville;
    
    private String pays;
    
    private Integer capacite;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    private String timezone;
}