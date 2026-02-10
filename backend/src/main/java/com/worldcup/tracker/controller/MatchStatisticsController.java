package com.worldcup.tracker.controller;

import java.util.List;

import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.service.IStatistiqueMatchService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class MatchStatisticsController {

    private final IStatistiqueMatchService statistiqueMatchService;

    public MatchStatisticsController(IStatistiqueMatchService statistiqueMatchService) {
        this.statistiqueMatchService = statistiqueMatchService;
    }

    @GetMapping("/match/{matchId}")
    public List<StatistiqueMatch> getStatistics(@PathVariable Long matchId) {
        return statistiqueMatchService.getByMatch(matchId);
    }
}
