package com.worldcup.tracker.service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorldCupService {

    private final RestTemplate restTemplate;
    public Object getAllMatches() {
        return List.of(
                Map.of(
                        "home_team", "France",
                        "away_team", "Brazil",
                        "datetime", "2026-06-12T18:00:00Z",
                        "status", "completed"
                ),
                Map.of(
                        "home_team", "Spain",
                        "away_team", "Germany",
                        "datetime", "2026-06-13T21:00:00Z",
                        "status", "upcoming"
                )
        );
    }

    public Object getTeamMatches(int teamId) {
        String url = "https://worldcupjson.net/teams/" + teamId + "/matches";
        return restTemplate.getForObject(url, Object.class);
    }
}
