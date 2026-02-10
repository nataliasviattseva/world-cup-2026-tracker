package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.MatchStatisticsDTO;
import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.repository.StatistiqueMatchRepository;
import com.worldcup.tracker.service.StatistiqueMatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchStatisticsServiceImpl implements StatistiqueMatchService {

    private final StatistiqueMatchRepository statisticsRepository;

    public MatchStatisticsServiceImpl(StatistiqueMatchRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }


    @Override
    public StatistiqueMatch getEntityById(Long id) {
        return return statisticsRepository.findById(id).orElse(null);
    }

    @Override
    public List<StatistiqueMatch> getAll() {
        return statisticsRepository.findAll();
    }

    @Override
    public List<StatistiqueMatch> getByMatch(Long matchId) {
        return statisticsRepository.findByMatchId(matchId);
    }

    @Override
    public List<StatistiqueMatch> getByEquipe(Long equipeId) {
        return List.of();
    }

    @Override
    public StatistiqueMatch save(StatistiqueMatch statistique) {
        return statisticsRepository.save(statistique);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public StatistiqueMatch getByMatchAndEquipe(Long matchId, Long equipeId) {
        return statisticsRepository.findByMatchIdAndEquipeId(matchId, equipeId).orElse(null);
    }

    @Override
    public void updateMatchStatistics(Long matchId, Long equipeId, int possession, int shots, int shotsOnTarget) {
        StatistiqueMatch stats = statisticsRepository.findByMatchIdAndEquipeId(matchId, equipeId)
                .orElse(new StatistiqueMatch());

        stats.setMatchId(matchId);
        stats.setEquipeId(equipeId);
        stats.setPossessionEquipe1(possession);
        stats.setTirsEquipe1(shots);
        stats.setTirsCadrésEquipe1(shotsOnTarget);

        statisticsRepository.save(stats);

    }

    @Override
    public List<StatistiqueMatch> getTopPerformances(String statType, int limit) {
        if ("shots".equalsIgnoreCase(statType)) {
            return statisticsRepository.findTopByShots(limit);
        }
        return List.of();
    }

    // pour mapper vers un DTO :
    private MatchStatisticsDTO mapToDTO(StatistiqueMatch stats) {
        return MatchStatisticsDTO.builder()
                .matchId(stats.getMatch().getId())
                .possessionTeam1(stats.getPossessionEquipe1())
                .possessionTeam2(stats.getPossessionEquipe2())
                .shotsTeam1(stats.getTirsEquipe1())
                .shotsTeam2(stats.getTirsEquipe2())
                .cornersTeam1(stats.getCornersEquipe1())
                .cornersTeam2(stats.getCornersEquipe2())
                .build();
    }
}
