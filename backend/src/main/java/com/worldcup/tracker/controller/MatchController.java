package com.worldcup.tracker.controller;

import com.worldcup.tracker.dto.MatchDTO;
import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.service.MatchService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<MatchDTO> getAllMatches() {

        return matchService.getAllMatches();
    }   

     // Matches by phase (GROUPS, QUARTER_FINAL, FINAL…)
    @GetMapping("/phase/{phase}")
    public List<MatchDTO> getMatchesByPhase(@PathVariable String phase) {
        return matchService.getMatchesByPhase(phase);
    }

    // Match details
    @GetMapping("/{id}")
    public MatchDTO getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id);
    }

    // Live / ongoing matches
    @GetMapping("/live")
    public List<MatchDTO> getLiveMatches() {
        return matchService.getLiveMatches();
    }
    
}
