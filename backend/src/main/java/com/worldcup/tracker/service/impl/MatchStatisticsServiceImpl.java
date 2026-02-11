package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.MatchStatisticsDTO;
import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.StatistiqueMatchRepository;
import com.worldcup.tracker.service.StatistiqueMatchService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MatchStatisticsServiceImpl implements StatistiqueMatchService {

    private final StatistiqueMatchRepository statisticsRepository;
    private final MatchRepository matchRepository;

    public MatchStatisticsServiceImpl(StatistiqueMatchRepository statisticsRepository,
                                      MatchRepository matchRepository) {
        this.statisticsRepository = statisticsRepository;
        this.matchRepository = matchRepository;
    }


    @Override
    public StatistiqueMatch getEntityById(Long id) {
        return statisticsRepository.findById(id).orElse(null);
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
    public StatistiqueMatch save(StatistiqueMatch statistique) {
        return statisticsRepository.save(statistique);
    }

    @Override
    public void delete(Long id) {
        statisticsRepository.deleteById(id);
    }

    @Override
    public StatistiqueMatch getByMatchAndEquipe(Long matchId, Long equipeId) {
        return null;
    }

    @Override
    public void updateMatchStatistics(Long matchId, int possession, int shots, int shotsOnTarget) {

        List<StatistiqueMatch> list = statisticsRepository.findByMatchId(matchId);
        StatistiqueMatch stats = list.isEmpty() ? new StatistiqueMatch() : list.get(0);

        stats.setMatch(matchRepository.getReferenceById(matchId));

        stats.setPossessionEquipe1(BigDecimal.valueOf(possession));
        stats.setTirsEquipe1(shots);
        stats.setTirsCadresEquipe1(shotsOnTarget);

        statisticsRepository.save(stats);

    }

    @Override
    public List<StatistiqueMatch> getTopPerformances(String statType, int limit) {
        if ("shots".equalsIgnoreCase(statType)) {
            return statisticsRepository.findTop10ByOrderByTirsEquipe1Desc();
        }
        return List.of();
    }

    // pour mapper vers un DTO :
    private MatchStatisticsDTO mapToDTO(StatistiqueMatch stats) {
        return MatchStatisticsDTO.builder()
                .matchId(stats.getMatch().getId())
                .possessionTeam1(stats.getPossessionEquipe1() != null ? stats.getPossessionEquipe1().intValue() : 0)
                .possessionTeam2(stats.getPossessionEquipe2() != null ? stats.getPossessionEquipe2().intValue() : 0)
                .shotsTeam1(stats.getTirsEquipe1())
                .shotsTeam2(stats.getTirsEquipe2())
                .cornersTeam1(stats.getCornersEquipe1())
                .cornersTeam2(stats.getCornersEquipe2())
                .build();
    }
}
