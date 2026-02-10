package com.worldcup.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;

@Service
public class PhaseService {

    private final PhaseCompetitionService phaseCompetitionService;

    public PhaseService(PhaseCompetitionService phaseCompetitionService) {
        this.phaseCompetitionService = phaseCompetitionService;
    }

    public List<PhaseCompetition> getAllPhases() {
        return phaseCompetitionService.getAll();
    }

    public PhaseCompetition getPhaseById(Long id) {
        return phaseCompetitionService.getEntityById(id);
    }

    public PhaseCompetition getCurrentPhase() {
        return phaseCompetitionService.getCurrentPhase();
    }

    public PhaseCompetition getPhaseByName(PhaseNomEnum nom) {
        return phaseCompetitionService.getByNom(nom);
    }

    public List<PhaseCompetition> getOrderedPhases() {
        return phaseCompetitionService.getOrderedBySequence();
    }
}
import com.worldcup.tracker.dto.PhaseDTO;
import java.util.List;

public interface PhaseService {
    List<PhaseDTO> getAllPhases();
}
