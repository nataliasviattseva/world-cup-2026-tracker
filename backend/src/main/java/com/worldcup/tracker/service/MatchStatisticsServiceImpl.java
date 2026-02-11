package com.worldcup.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.dto.StatistiqueMatchDTO;
import com.worldcup.tracker.dto.DTOMapper;
import com.worldcup.tracker.model.StatistiqueMatch;
import com.worldcup.tracker.repository.StatistiqueMatchRepository;
import com.worldcup.tracker.service.IMatchStatisticsService;
@Service
public class MatchStatisticsServiceImpl implements IMatchStatisticsService {

    private final StatistiqueMatchRepository statisticsRepository;
    private final DTOMapper dtoMapper;

    public MatchStatisticsServiceImpl(StatistiqueMatchRepository statisticsRepository, 
                                     DTOMapper dtoMapper) {
        this.statisticsRepository = statisticsRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public StatistiqueMatchDTO getStatisticsForMatch(Long matchId) {
    //     return statisticsRepository.findByMatchId(matchId)
    //             .map(dtoMapper::mapToMatchStatisticsDTO)
    //             .orElse(null);
                
    // }
            List<StatistiqueMatch> statistics = statisticsRepository.findByMatchId(matchId);
        if (statistics.isEmpty()) {
            return null;
        }
        return dtoMapper.mapToMatchStatisticsDTO(statistics.get(0));
    }

    @Override
    public List<StatistiqueMatch> getStatisticsByMatchId(Long matchId) {
        return statisticsRepository.findByMatchId(matchId)
                // .map(List::of)
                // .orElse(List.of());
                .stream()
                .toList();
                
    }

    @Override
    public StatistiqueMatch getStatisticsById(Long id) {
        return statisticsRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<StatistiqueMatch> getStatisticsByTeam(Long equipeId) {
        // This would need a custom repository method
        return List.of();
    }

    @Override
    public StatistiqueMatch getMatchTeamStatistics(Long matchId, Long equipeId) {
        // This would need a custom repository method
        return null;
    }

    @Override
    public void updateMatchStatistics(Long matchId, Long equipeId, int possession, int shots, int shotsOnTarget) {
    //     statistiqueMatchService.updateMatchStatistics(matchId, equipeId, possession, shots, shotsOnTarget);
    // }

    // private StatistiqueMatchDTO mapToDTO(StatistiqueMatch stats) {
    //     return StatistiqueMatchDTO.builder()
    //             .matchId(stats.getMatch().getId())
    //             .possessionTeam1(stats.getPossessionEquipe1() != null ? stats.getPossessionEquipe1().intValue() : 0)
    //             .possessionTeam2(stats.getPossessionEquipe2() != null ? stats.getPossessionEquipe2().intValue() : 0)
    //             .shotsTeam1(stats.getTirsEquipe1())
    //             .shotsTeam2(stats.getTirsEquipe2())
    //             .cornersTeam1(stats.getCornersEquipe1())
    //             .cornersTeam2(stats.getCornersEquipe2())
    //             .build();
    }
}
