package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.dto.StatistiqueMatchDTO;
import com.worldcup.tracker.model.StatistiqueMatch;

public interface IMatchStatisticsService {

    StatistiqueMatchDTO getStatisticsForMatch(Long matchId);

    List<StatistiqueMatch> getStatisticsByMatchId(Long matchId);

    StatistiqueMatch getStatisticsById(Long id);

    List<StatistiqueMatch> getStatisticsByTeam(Long equipeId);

    StatistiqueMatch getMatchTeamStatistics(Long matchId, Long equipeId);

    void updateMatchStatistics(Long matchId, Long equipeId, int possession, int shots, int shotsOnTarget);
}
