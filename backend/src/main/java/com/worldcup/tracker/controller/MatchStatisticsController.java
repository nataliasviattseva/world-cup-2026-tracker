package com.worldcup.tracker.controller;

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
