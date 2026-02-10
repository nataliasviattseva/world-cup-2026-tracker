package com.worldcup.tracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhaseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer matchCount;
}
