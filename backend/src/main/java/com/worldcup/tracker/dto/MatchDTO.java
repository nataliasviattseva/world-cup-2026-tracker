package com.worldcup.tracker.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MatchDTO {
    private Long id;
    private String phase;
    private TeamDTO teamA;
    private TeamDTO teamB;
    private String date;
    private String time;
    private String stadium;
    private String city;
    private String status; // 'upcoming', 'live', 'finished'
    private Integer currentMinute;
    private List<MatchEventDTO> events;
}
