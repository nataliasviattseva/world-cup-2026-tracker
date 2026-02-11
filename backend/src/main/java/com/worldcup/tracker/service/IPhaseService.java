package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.dto.PhaseCompetitionDTO;
import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;

public interface IPhaseService {

    List<PhaseCompetitionDTO> getAllPhases();

    List<PhaseCompetition> getAllPhasesEntity();

    PhaseCompetition getPhaseById(Long id);

    PhaseCompetition getCurrentPhase();

    PhaseCompetition getPhaseByName(PhaseNomEnum nom);

    List<PhaseCompetition> getOrderedPhases();
}
