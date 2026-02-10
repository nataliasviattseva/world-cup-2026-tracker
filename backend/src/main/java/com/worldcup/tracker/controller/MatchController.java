package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.service.MatchService;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }   

     // Matches by phase (GROUPS, QUARTER_FINAL, FINAL…)
    @GetMapping("/phase/{phase}")
    public List<Match> getMatchesByPhase(@PathVariable String phase) {
        return matchService.getMatchesByPhase(phase);
    }

    // Match details
    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id);
    }

    // Live / ongoing matches
    @GetMapping("/live")
    public List<Match> getLiveMatches() {
        return matchService.getLiveMatches();
    }
    
}
