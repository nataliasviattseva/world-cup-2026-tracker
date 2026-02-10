package com.worldcup.tracker.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // All matches ordered
    List<Match> findAllByOrderByDateHeureAsc();

    // By phase
    List<Match> findByPhaseIdOrderByDateHeureAsc(Long phaseId);

    // By group (GROUP A, B, C…)
    List<Match> findByGroupeOrderByDateHeureAsc(String groupe);

    // Live / ongoing
    List<Match> findByStatutOrderByDateHeureAsc(StatutMatchEnum statut);

    // By team (home or away)
    List<Match> findByEquipe1IdOrEquipe2Id(Long equipe1Id, Long equipe2Id);

    // Time range (optional)
    List<Match> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);

    // Time range + ordered
    List<Match> findByDateHeureBetweenOrderByDateHeureAsc(LocalDateTime start, LocalDateTime end);

}
