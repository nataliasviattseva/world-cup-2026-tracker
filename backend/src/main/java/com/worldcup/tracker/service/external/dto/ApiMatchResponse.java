package com.worldcup.tracker.service.external.dto;

import lombok.Data;

@Data
public class ApiMatchResponse {

    private Fixture fixture;
    private Teams teams;

    @Data
    public static class Fixture {
        private String date;
        private Status status;
    }

    @Data
    public static class Status {
        private String longStatus;
        private String shortStatus;
    }

    @Data
    public static class Teams {
        private ApiTeam home;
        private ApiTeam away;
    }
}
