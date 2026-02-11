package com.worldcup.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    private Long id;
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
