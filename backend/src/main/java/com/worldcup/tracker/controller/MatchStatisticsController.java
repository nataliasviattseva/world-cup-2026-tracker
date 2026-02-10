package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.service.StatistiqueMatchService;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class MatchStatisticsController {

    private final StatistiqueMatchService statistiqueMatchService;

    public MatchStatisticsController(StatistiqueMatchService statistiqueMatchService) {
        this.statistiqueMatchService = statistiqueMatchService;
    }

    @GetMapping("/match/{matchId}")
    public List<StatistiqueMatch> getStatistics(@PathVariable Long matchId) {
        return statistiqueMatchService.getByMatch(matchId);
    }
}
