package com.worldcup.tracker.service;

import java.time.LocalDateTime;
import java.util.List;

import com.worldcup.tracker.dto.MatchDTO;
import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;

public interface IMatchService {

    Match getEntityById(Long id);

    List<Match> getAll();

    List<Match> getAllOrderedByKickoff();

    List<Match> getByPhase(Long phaseId);

    List<MatchDTO> getByGroupe(String groupe);

    List<MatchDTO> getByStatut(StatutMatchEnum statut);

    List<MatchDTO> getLiveMatches();

    List<MatchDTO> getByEquipe(Long equipeId);

    List<MatchDTO> getBetween(LocalDateTime start, LocalDateTime end);

    Match save(Match match);

    Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut);

    void delete(Long id);

    List<MatchDTO> getAllMatches();

    List<MatchDTO> getMatchesByPhase(String phaseName);

    MatchDTO getMatchById(Long id);
}