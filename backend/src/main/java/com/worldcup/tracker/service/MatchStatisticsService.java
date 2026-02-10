package com.worldcup.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.StatistiqueMatch;

@Service
public class MatchStatisticsService {

    private final StatistiqueMatchService statistiqueMatchService;

    public MatchStatisticsService(StatistiqueMatchService statistiqueMatchService) {
        this.statistiqueMatchService = statistiqueMatchService;
    }

    public List<StatistiqueMatch> getStatisticsByMatchId(Long matchId) {
        return statistiqueMatchService.getByMatch(matchId);
    }

    public StatistiqueMatch getStatisticsById(Long id) {
        return statistiqueMatchService.getEntityById(id);
    }

    public List<StatistiqueMatch> getStatisticsByTeam(Long equipeId) {
        return statistiqueMatchService.getByEquipe(equipeId);
    }

    public StatistiqueMatch getMatchTeamStatistics(Long matchId, Long equipeId) {
        return statistiqueMatchService.getByMatchAndEquipe(matchId, equipeId);
    }

    public void updateMatchStatistics(Long matchId, Long equipeId, int possession, int shots, int shotsOnTarget) {
        statistiqueMatchService.updateMatchStatistics(matchId, equipeId, possession, shots, shotsOnTarget);
    }
}