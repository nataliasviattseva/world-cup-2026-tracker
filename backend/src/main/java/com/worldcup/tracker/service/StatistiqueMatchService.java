package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.model.StatistiqueMatch;

public interface StatistiqueMatchService {

    StatistiqueMatch getEntityById(Long id);

    List<StatistiqueMatch> getAll();

    List<StatistiqueMatch> getByMatch(Long matchId);

    StatistiqueMatch save(StatistiqueMatch statistique);

    void delete(Long id);

    StatistiqueMatch getByMatchAndEquipe(Long matchId, Long equipeId);

    void updateMatchStatistics(Long matchId, int possession, int shots, int shotsOnTarget);

    List<StatistiqueMatch> getTopPerformances(String statType, int limit);
}