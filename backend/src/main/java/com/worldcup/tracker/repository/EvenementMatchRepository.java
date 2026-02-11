package com.worldcup.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.EvenementMatch;

public interface EvenementMatchRepository extends JpaRepository<EvenementMatch, Long> {

    List<EvenementMatch> findByMatchIdOrderByMinuteAscMinuteAdditionnelleAsc(Long matchId);

    List<EvenementMatch> findByEquipeId(Long equipeId);
}
