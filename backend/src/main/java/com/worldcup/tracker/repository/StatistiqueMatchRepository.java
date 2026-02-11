package com.worldcup.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.StatistiqueMatch;

public interface StatistiqueMatchRepository extends JpaRepository<StatistiqueMatch, Long> {

    List<StatistiqueMatch> findByMatchId(Long matchId);
    List<StatistiqueMatch> findTop10ByOrderByTirsEquipe1Desc();



}
