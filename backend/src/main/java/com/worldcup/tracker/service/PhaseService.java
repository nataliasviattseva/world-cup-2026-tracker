package com.worldcup.tracker.service;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;

import java.util.List;

public interface PhaseService {

    List<PhaseCompetition> getAllPhases();
    PhaseCompetition getPhaseById(Long id);
    PhaseCompetition getCurrentPhase();
    PhaseCompetition getPhaseByName(PhaseNomEnum nom);
    List<PhaseCompetition> getOrderedPhases();
}
