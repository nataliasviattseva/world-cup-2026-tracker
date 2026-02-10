package com.worldcup.tracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamDTO {
    private Long id;
    private String name;
    private String flag;
    private String code;
    private Integer score; // Used when nested in MatchDTO
}
