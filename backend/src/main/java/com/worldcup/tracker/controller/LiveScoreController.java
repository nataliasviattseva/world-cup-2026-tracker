package com.worldcup.tracker.controller;

import com.worldcup.tracker.service.LiveScoreApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/livescore")
public class LiveScoreController {

    private final LiveScoreApiService liveScoreApiService;

    public LiveScoreController(LiveScoreApiService liveScoreApiService) {
        this.liveScoreApiService = liveScoreApiService;
    }

    /**
     * GET /api/livescore/live
     * Get all live scores
     */
    @GetMapping(value = "/live", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLiveScores() {
        try {
            String response = liveScoreApiService.getLiveScores();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching live scores: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch live scores\"}");
        }
    }

    /**
     * GET /api/livescore/competitions
     * Get all competitions
     */
    @GetMapping(value = "/competitions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCompetitions() {
        try {
            String response = liveScoreApiService.getCompetitions();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching competitions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch competitions\"}");
        }
    }

    /**
     * GET /api/livescore/matches/{matchId}
     * Get match details by ID
     */
    @GetMapping(value = "/matches/{matchId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMatchDetails(@PathVariable String matchId) {
        try {
            String response = liveScoreApiService.getMatchDetails(matchId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching match details for ID {}: {}", matchId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch match details\"}");
        }
    }

    /**
     * GET /api/livescore/teams/{teamId}
     * Get team details by ID
     */
    @GetMapping(value = "/teams/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTeamDetails(@PathVariable String teamId) {
        try {
            String response = liveScoreApiService.getTeamDetails(teamId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching team details for ID {}: {}", teamId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch team details\"}");
        }
    }

    /**
     * GET /api/livescore/fixtures
     * Get all fixtures
     */
    @GetMapping(value = "/fixtures", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFixtures() {
        try {
            String response = liveScoreApiService.getFixtures();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching fixtures: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch fixtures\"}");
        }
    }
    
    /**
     * GET /api/livescore/history
     * Get match history
     */
    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMatchHistory() {
        try {
            String response = liveScoreApiService.getMatchHistory();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching match history: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch match history\"}");
        }
    }
    
    /**
     * GET /api/livescore/matches/{matchId}/events
     * Get match events by ID
     */
    @GetMapping(value = "/matches/{matchId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMatchEvents(@PathVariable String matchId) {
        try {
            String response = liveScoreApiService.getMatchEvents(matchId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching match events for ID {}: {}", matchId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch match events\"}");
        }
    }
    
    /**
     * GET /api/livescore/matches/{matchId}/lineups
     * Get match lineups by ID
     */
    @GetMapping(value = "/matches/{matchId}/lineups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMatchLineups(@PathVariable String matchId) {
        try {
            String response = liveScoreApiService.getMatchLineups(matchId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching match lineups for ID {}: {}", matchId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch match lineups\"}");
        }
    }
    
    /**
     * GET /api/livescore/matches/{matchId}/commentary
     * Get match commentary by ID
     */
    @GetMapping(value = "/matches/{matchId}/commentary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMatchCommentary(@PathVariable String matchId) {
        try {
            String response = liveScoreApiService.getMatchCommentary(matchId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching match commentary for ID {}: {}", matchId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch match commentary\"}");
        }
    }
    
    /**
     * GET /api/livescore/teams
     * Get all teams
     */
    @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTeams() {
        try {
            String response = liveScoreApiService.getTeams();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching teams: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch teams\"}");
        }
    }
    
    /**
     * GET /api/livescore/head2head
     * Get head to head data
     */
    @GetMapping(value = "/head2head", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getHeadToHead(
            @RequestParam String team1Id,
            @RequestParam String team2Id) {
        try {
            String response = liveScoreApiService.getHeadToHead(team1Id, team2Id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching head to head for teams {} vs {}: {}", team1Id, team2Id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch head to head\"}");
        }
    }

    /**
     * GET /api/livescore/standings/{competitionId}
     * Get standings by competition ID
     */
    @GetMapping(value = "/standings/{competitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStandings(@PathVariable String competitionId) {
        try {
            String response = liveScoreApiService.getStandings(competitionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching standings for competition {}: {}", competitionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to fetch standings\"}");
        }
    }

    /**
     * DELETE /api/livescore/cache
     * Clear all cache entries
     */
    @DeleteMapping("/cache")
    public ResponseEntity<String> clearAllCache() {
        try {
            liveScoreApiService.clearAllCache();
            return ResponseEntity.ok("{\"message\": \"All cache cleared successfully\"}");
        } catch (Exception e) {
            log.error("Error clearing cache: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to clear cache\"}");
        }
    }

    /**
     * DELETE /api/livescore/cache/{endpoint}
     * Clear cache for specific endpoint
     */
    @DeleteMapping("/cache/{endpoint}")
    public ResponseEntity<String> clearCacheForEndpoint(@PathVariable String endpoint) {
        try {
            liveScoreApiService.clearCacheForEndpoint("/" + endpoint);
            return ResponseEntity.ok("{\"message\": \"Cache cleared for endpoint: " + endpoint + "\"}");
        } catch (Exception e) {
            log.error("Error clearing cache for endpoint {}: {}", endpoint, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Failed to clear cache for endpoint\"}");
        }
    }
}
