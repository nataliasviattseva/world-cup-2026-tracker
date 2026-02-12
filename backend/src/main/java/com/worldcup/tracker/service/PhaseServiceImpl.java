package com.worldcup.tracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.dto.PhaseCompetitionDTO;
import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;

@Service
public class PhaseServiceImpl implements IPhaseService {

    private final PhaseCompetitionRepository phaseRepository;
    private final PhaseCompetitionServiceImpl phaseCompetitionService;
    private final MatchRepository matchRepository;

    public PhaseServiceImpl(PhaseCompetitionRepository phaseRepository,
                           PhaseCompetitionServiceImpl phaseCompetitionService,
                           MatchRepository matchRepository) {
        this.phaseRepository = phaseRepository;
        this.phaseCompetitionService = phaseCompetitionService;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<PhaseCompetitionDTO> getAllPhases() {
        return phaseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PhaseCompetition> getAllPhasesEntity() {
        return phaseCompetitionService.getAll();
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

    private PhaseCompetitionDTO mapToDTO(PhaseCompetition phase) {
        // Dynamically count matches instead of relying on static field
        long matchCount = matchRepository.countByPhaseId(phase.getId());
        return PhaseCompetitionDTO.builder()
                .id(phase.getId())
                .nom(convertPhaseNom(phase.getNom()))
                .ordre(phase.getOrdre())
                .dateDebut(phase.getDateDebut())
                .dateFin(phase.getDateFin())
                .description(phase.getDescription())
                .nombreMatchs((int) matchCount)
                .build();
    }

    private PhaseCompetitionDTO.PhaseNomDTO convertPhaseNom(PhaseNomEnum phaseNom) {
        if (phaseNom == null) return null;
        return PhaseCompetitionDTO.PhaseNomDTO.valueOf(phaseNom.name());
    }
}
