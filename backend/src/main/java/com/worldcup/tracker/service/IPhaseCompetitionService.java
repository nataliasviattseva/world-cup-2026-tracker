package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;

public interface IPhaseCompetitionService {

    PhaseCompetition getEntityById(Long id);

    List<PhaseCompetition> getAll();

    PhaseCompetition getByNom(PhaseNomEnum nom);

    PhaseCompetition getCurrentPhase();

    List<PhaseCompetition> getOrderedBySequence();

    PhaseCompetition save(PhaseCompetition phase);

    void delete(Long id);

    void activatePhase(Long phaseId);

    PhaseCompetition getNextPhase(Long currentPhaseId);

    boolean isPhaseComplete(Long phaseId);
}