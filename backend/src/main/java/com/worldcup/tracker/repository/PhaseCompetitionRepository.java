package com.worldcup.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;

public interface PhaseCompetitionRepository extends JpaRepository<PhaseCompetition, Long> {

    Optional<PhaseCompetition> findByNom(PhaseNomEnum nom);
    
    List<PhaseCompetition> findAllByOrderByOrdreAsc();
    
    Optional<PhaseCompetition> findByOrdreGreaterThanOrderByOrdreAsc(Integer ordre);

}
