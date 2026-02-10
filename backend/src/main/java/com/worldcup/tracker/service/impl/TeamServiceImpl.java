package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.TeamDTO;
import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.repository.EquipeRepository;
import com.worldcup.tracker.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final EquipeRepository equipeRepository;

    public TeamServiceImpl(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return equipeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        return equipeRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    private TeamDTO mapToDTO(Equipe equipe) {
        return TeamDTO.builder()
                .id(equipe.getId())
                .name(equipe.getNom())
                .flag(equipe.getDrapeauUrl()) // Assuming flag is stored as emoji or URL in DB
                .code(equipe.getCodePays())
                .build();
    }
}
