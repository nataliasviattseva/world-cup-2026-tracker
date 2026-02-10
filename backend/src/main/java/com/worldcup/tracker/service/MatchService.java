package com.worldcup.tracker.service;

import java.time.LocalDateTime;
import java.util.List;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;
import com.worldcup.tracker.dto.MatchDTO;

public interface MatchService {

    Match getEntityById(Long id);

    List<MatchDTO> getAllMatches();

    List<Match> getAllOrderedByKickoff();

    List<MatchDTO> getMatchesByPhase(String phase);

    List<Match> getByGroupe(String groupe);

    List<Match> getByStatut(StatutMatchEnum statut);

    List<MatchDTO> getLiveMatches();

    List<Match> getByEquipe(Long equipeId);

    List<Match> getBetween(LocalDateTime start, LocalDateTime end);

    Match save(Match match);

    Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut);

    void delete(Long id);

    MatchDTO getMatchById(Long id);
}
