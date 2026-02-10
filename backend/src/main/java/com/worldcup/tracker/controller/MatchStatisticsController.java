package com.worldcup.tracker.controller;

import com.worldcup.tracker.dto.MatchStatisticsDTO;
import com.worldcup.tracker.service.MatchStatisticsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class MatchStatisticsController {

    private final MatchStatisticsService statisticsService;

    public MatchStatisticsController(MatchStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/match/{matchId}")
    public MatchStatisticsDTO getStatistics(@PathVariable Long matchId) {
        return statisticsService.getStatisticsForMatch(matchId);
    }
}
