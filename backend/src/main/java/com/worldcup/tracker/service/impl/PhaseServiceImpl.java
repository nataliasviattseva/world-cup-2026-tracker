package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.PhaseDTO;
import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;
import com.worldcup.tracker.service.PhaseCompetitionService;
import com.worldcup.tracker.service.PhaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhaseServiceImpl implements PhaseService {

    private final PhaseCompetitionService phaseCompetitionService;

    public PhaseServiceImpl(PhaseCompetitionService phaseCompetitionService) {
        this.phaseCompetitionService = phaseCompetitionService;
    }

    @Override
    public List<PhaseDTO> getAllPhases() {
        return phaseCompetitionService.getAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PhaseCompetition getPhaseById(Long id) {
        return phaseCompetitionService.getEntityById(id);
    }

    @Override
    public PhaseCompetition getCurrentPhase() {
        return phaseCompetitionService.getCurrentPhase();
    }

    @Override
    public PhaseCompetition getPhaseByName(PhaseNomEnum nom) {
        return phaseCompetitionService.getByNom(nom);
    }

    @Override
    public List<PhaseCompetition> getOrderedPhases() {
        return phaseCompetitionService.getOrderedBySequence();
    }

    private PhaseDTO mapToDTO(PhaseCompetition phase) {
        return PhaseDTO.builder()
                .id(phase.getId())
                .name(phase.getNom().name())
                .description(phase.getDescription())
                .matchCount(phase.getNombreMatchs())
                .build();
    }
}
