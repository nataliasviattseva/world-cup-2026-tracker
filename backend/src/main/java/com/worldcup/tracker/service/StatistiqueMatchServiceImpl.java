package com.worldcup.tracker.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.repository.StatistiqueMatchRepository;

@Service
public class StatistiqueMatchServiceImpl implements IStatistiqueMatchService {

    private final StatistiqueMatchRepository statistiqueRepository;

    public StatistiqueMatchServiceImpl(StatistiqueMatchRepository statistiqueRepository) {
        this.statistiqueRepository = statistiqueRepository;
    }

    @Override
    public StatistiqueMatch getEntityById(Long id) {
        return statistiqueRepository.findById(id).orElse(null);
    }

    @Override
    public List<StatistiqueMatch> getAll() {
        return statistiqueRepository.findAll();
    }

    @Override
    public List<StatistiqueMatch> getByMatch(Long matchId) {
        return statistiqueRepository.findByMatchId(matchId);
    }

    @Override
    public List<StatistiqueMatch> getByEquipe(Long equipeId) {
        return List.of();
    }

    @Override
    public StatistiqueMatch save(StatistiqueMatch statistique) {
        return statistiqueRepository.save(statistique);
    }

    @Override
    public void delete(Long id) {
        statistiqueRepository.deleteById(id);
    }

    @Override
    public StatistiqueMatch getByMatchAndEquipe(Long matchId, Long equipeId) {
        List<StatistiqueMatch> statsList = getByMatch(matchId);
        return statsList.isEmpty() ? null : statsList.get(0);
    }

    @Override
    public void updateMatchStatistics(Long matchId, Long equipeId, int possession, int shots, int shotsOnTarget) {
        List<StatistiqueMatch> statsList = getByMatch(matchId);
        if (!statsList.isEmpty()) {
            StatistiqueMatch stats = statsList.get(0); // Get the first (and should be only) stat record
            // Logic would need to be added to determine if this is for equipe1 or equipe2
            // For now, update as if it's for equipe1
            stats.setPossessionEquipe1(BigDecimal.valueOf(possession));
            stats.setTirsEquipe1(shots);
            stats.setTirsCadresEquipe1(shotsOnTarget);
            save(stats);
        }
    }

    @Override
    public List<StatistiqueMatch> getTopPerformances(String statType, int limit) {
        List<StatistiqueMatch> allStats = statistiqueRepository.findAll();
        return allStats.stream().limit(limit).toList();
    }
}