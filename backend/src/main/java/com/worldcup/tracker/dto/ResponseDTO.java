package com.worldcup.tracker.dto;

import lombok.*;

import java.util.List;

/**
 * Response wrapper DTOs for API endpoints
 */
public class ResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchesResponse {
        private List<MatchDTO> matches;
        private Integer totalCount;
        private String phase;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchesSummaryResponse {
        private List<MatchSummaryDTO> matches;
        private Integer totalCount;
        private String phase;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamsResponse {
        private List<EquipeDTO> teams;
        private Integer totalCount;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GroupsResponse {
        private List<GroupeDTO> groups;
        private List<ClassementGroupeDTO> standings;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StadiumsResponse {
        private List<StadeDTO> stadiums;
        private Integer totalCount;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PhasesResponse {
        private List<PhaseCompetitionDTO> phases;
        private Integer totalCount;
        private String message;
    }
}