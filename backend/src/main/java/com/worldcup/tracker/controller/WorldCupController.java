package com.worldcup.tracker.controller;

import com.worldcup.tracker.dto.MatchRequest;
import com.worldcup.tracker.service.external.WorldCupService;
import com.worldcup.tracker.utils.InputSanitizer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/worldcup")
@RequiredArgsConstructor
public class WorldCupController {

    private final WorldCupService worldCupService;

    @Value("${app.admin.token}")
    private String adminToken;


    @GetMapping("/matches")
    public Object getMatches() {
        return worldCupService.getAllMatches();
    }

    @GetMapping("/team/{id}")
    public Object getTeamMatches(@PathVariable int id) {
        return worldCupService.getTeamMatches(id);
    }

    @PostMapping("/api/worldcup/matches")
    public ResponseEntity<?> createMatch(
            @RequestHeader("X-ADMIN-TOKEN") String token,
            @Valid @RequestBody MatchRequest request) {

        String home = InputSanitizer.clean(request.getHomeTeam());
        String away = InputSanitizer.clean(request.getAwayTeam());
        String phase = InputSanitizer.clean(request.getPhase());

        // Transforme le DTO en entité ou en objet métier
        System.out.println("Match reçu : " + request);

        return ResponseEntity.ok("Match créé avec succès");
    }
}
