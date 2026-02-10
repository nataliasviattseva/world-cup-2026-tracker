package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.PhaseDTO;
import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;
import com.worldcup.tracker.service.PhaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhaseServiceImpl implements PhaseService {

    private final PhaseCompetitionRepository phaseRepository;

    public PhaseServiceImpl(PhaseCompetitionRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    @Override
    public List<PhaseDTO> getAllPhases() {
        return phaseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PhaseDTO mapToDTO(PhaseCompetition phase) {
        return PhaseDTO.builder()
                .id(phase.getId())
                .name(phase.getNom().name()) // You might want to format this string better
                .description(phase.getDescription())
                .matchCount(phase.getNombreMatchs())
                .build();
    }
}
