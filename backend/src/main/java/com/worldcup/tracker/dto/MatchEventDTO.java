package com.worldcup.tracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchEventDTO {
    private Integer minute;
    private String type; // 'goal', 'yellow_card', 'red_card', 'substitution'
    private String team; // 'A' or 'B'
    private String player;
    private String description;
}
