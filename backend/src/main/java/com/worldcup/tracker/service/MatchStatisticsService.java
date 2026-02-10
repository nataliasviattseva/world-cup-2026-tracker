package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.dto.MatchStatisticsDTO;
import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.StatistiqueMatch;

@Service
public class MatchStatisticsService implements IMatchStatisticsService {

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

    @Override
    public MatchStatisticsDTO getStatisticsForMatch(Long matchId) {
        return null;
    }
}
