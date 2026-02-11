package com.worldcup.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.StatistiqueMatch;

public interface StatistiqueMatchRepository extends JpaRepository<StatistiqueMatch, Long> {

    List<StatistiqueMatch> findByMatchId(Long matchId);

}
