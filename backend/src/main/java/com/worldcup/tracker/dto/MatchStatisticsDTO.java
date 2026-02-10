package com.worldcup.tracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchStatisticsDTO {
    private Long matchId;
    private Integer possessionTeam1;
    private Integer possessionTeam2;
    private Integer shotsTeam1;
    private Integer shotsTeam2;
    private Integer cornersTeam1;
    private Integer cornersTeam2;
}
