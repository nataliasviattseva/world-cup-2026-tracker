package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.MatchStatisticsDTO;
import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.repository.StatistiqueMatchRepository;
import com.worldcup.tracker.service.MatchStatisticsService;
import org.springframework.stereotype.Service;

@Service
public class MatchStatisticsServiceImpl implements MatchStatisticsService {

    private final StatistiqueMatchRepository statisticsRepository;

    public MatchStatisticsServiceImpl(StatistiqueMatchRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public MatchStatisticsDTO getStatisticsForMatch(Long matchId) {
        return statisticsRepository.findByMatchId(matchId)
                .map(this::mapToDTO)
                .orElse(null);
    }

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
