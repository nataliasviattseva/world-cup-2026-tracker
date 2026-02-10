package com.worldcup.tracker.service;

import java.time.LocalDateTime;
import java.util.List;

import com.worldcup.tracker.model.StatutMatchEnum;

public interface MatchService {

    Match getEntityById(Long id);

    List<Match> getAll();

    List<Match> getAllOrderedByKickoff();

    List<Match> getByPhase(Long phaseId);

    List<Match> getByGroupe(String groupe);

    List<Match> getByStatut(StatutMatchEnum statut);

    List<Match> getLiveMatches();

    List<Match> getByEquipe(Long equipeId);

    List<Match> getBetween(LocalDateTime start, LocalDateTime end);

    Match save(Match match);

    Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut);

    void delete(Long id);
}