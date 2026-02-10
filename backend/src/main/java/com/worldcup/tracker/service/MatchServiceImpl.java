package com.worldcup.tracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final PhaseCompetitionRepository phaseRepository;

    public MatchServiceImpl(MatchRepository matchRepository,
            PhaseCompetitionRepository phaseRepository) {
        this.matchRepository = matchRepository;
        this.phaseRepository = phaseRepository;
    }

    @Override
    public Match getEntityById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found: " + id));
    }

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getAllMatches() {
        return getAll();
    }

    @Override
    public List<Match> getAllOrderedByKickoff() {
        return matchRepository.findAllByOrderByDateHeureAsc();
    }

    @Override
    public List<Match> getByPhase(Long phaseId) {
        if (!phaseRepository.existsById(phaseId)) {
            throw new EntityNotFoundException("Phase not found: " + phaseId);
        }
        return matchRepository.findByPhaseIdOrderByDateHeureAsc(phaseId);
    }

    @Override
    public List<Match> getMatchesByPhase(String phase) {
        if (phase == null || phase.isBlank())
            return List.of();
        return matchRepository.findByPhaseNomOrderByDateHeureAsc(phase.trim());
    }

    @Override
    public Match getMatchById(Long id) {
        return getEntityById(id);
    }

    @Override
    public List<Match> getByGroupe(String groupe) {
        if (groupe == null || groupe.isBlank())
            return List.of();
        return matchRepository.findByGroupeOrderByDateHeureAsc(groupe.trim());
    }

    @Override
    public List<Match> getByStatut(StatutMatchEnum statut) {
        if (statut == null)
            return List.of();
        return matchRepository.findByStatutOrderByDateHeureAsc(statut);
    }

    @Override
    public List<Match> getLiveMatches() {
        return matchRepository.findByStatutOrderByDateHeureAsc(StatutMatchEnum.EN_COURS);
    }

    @Override
    public List<Match> getByEquipe(Long equipeId) {
        if (equipeId == null)
            return List.of();
        return matchRepository.findByEquipe1IdOrEquipe2Id(equipeId, equipeId);
    }

    @Override
    public List<Match> getBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || end.isBefore(start))
            return List.of();
        return matchRepository.findByDateHeureBetweenOrderByDateHeureAsc(start, end);
    }

    @Override
    public Match save(Match match) {
        if (match == null)
            throw new IllegalArgumentException("match must not be null");
        if (match.getEquipe1() == null || match.getEquipe2() == null) {
            throw new IllegalArgumentException("Match must have equipe1 and equipe2");
        }
        if (match.getEquipe1().getId().equals(match.getEquipe2().getId())) {
            throw new IllegalArgumentException("equipe1 and equipe2 must be different");
        }
        if (match.getPhase() == null)
            throw new IllegalArgumentException("Match must have a phase");
        if (match.getStade() == null)
            throw new IllegalArgumentException("Match must have a stade");
        if (match.getDateHeure() == null)
            throw new IllegalArgumentException("dateHeure is required");
        if (match.getStatut() == null)
            throw new IllegalArgumentException("statut is required");

        return matchRepository.save(match);
    }

    @Override
    public Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut) {
        Match match = getEntityById(matchId);

        match.setScoreEquipe1(score1);
        match.setScoreEquipe2(score2);

        if (statut != null) {
            match.setStatut(statut);
        }
        return matchRepository.save(match);
    }

    @Override
    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new EntityNotFoundException("Match not found: " + id);
        }
        matchRepository.deleteById(id);
    }

}
