package com.worldcup.tracker.service;

import com.worldcup.tracker.dto.PhaseDTO;
import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;

import java.util.List;

public interface PhaseService {

    List<PhaseDTO> getAllPhases();
    PhaseCompetition getPhaseById(Long id);
    PhaseCompetition getCurrentPhase();
    PhaseCompetition getPhaseByName(PhaseNomEnum nom);
    List<PhaseCompetition> getOrderedPhases();
}
