package com.worldcup.tracker.controller;

import java.util.List;

import com.worldcup.tracker.model.StatistiqueMatch;
<<<<<<< HEAD
import com.worldcup.tracker.service.IStatistiqueMatchService;
=======
import com.worldcup.tracker.service.StatistiqueMatchService;
import org.springframework.web.bind.annotation.CrossOrigin;
>>>>>>> 9a381468b38f76c71854414351345a9d0529e825
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
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
