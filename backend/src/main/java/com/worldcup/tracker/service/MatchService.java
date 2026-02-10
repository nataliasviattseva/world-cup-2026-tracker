package com.worldcup.tracker.service;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;
import com.worldcup.tracker.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchService implements IMatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match getEntityById(Long id) {
        return matchRepository.findById(id) .orElseThrow(() -> new RuntimeException("Match not found"));
    }

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getAllOrderedByKickoff() {
        return matchRepository.findAllByOrderByDateHeureAsc();
    }

    @Override
    public List<Match> getByPhase(Long phaseId) {
        return matchRepository.findByPhaseId(phaseId);
    }

    @Override
    public List<Match> getByGroupe(String groupe) {
        return matchRepository.findByGroupe(groupe);
    }

    @Override
    public List<Match> getByStatut(StatutMatchEnum statut) {
        return matchRepository.findByStatut(statut);
    }

    @Override
    public List<Match> getLiveMatches() {
        return matchRepository.findByStatut(StatutMatchEnum.EN_COURS);
    }

    @Override
    public List<Match> getByEquipe(Long equipeId) {
        return matchRepository.findByEquipe1IdOrEquipe2Id(equipeId, equipeId);
    }

    @Override
    public List<Match> getBetween(LocalDateTime start, LocalDateTime end) {
        return matchRepository.findByDateHeureBetween(start, end);
    }

    @Override
    public Match save(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut) {
        Match match = getEntityById(matchId);
        match.setScoreEquipe1(score1);
        match.setScoreEquipe2(score2);
        match.setStatut(statut);
        return matchRepository.save(match);
    }

    @Override
    public void delete(Long id) {
        matchRepository.deleteById(id);
    }
}
