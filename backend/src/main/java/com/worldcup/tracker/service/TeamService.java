package com.worldcup.tracker.service;

import com.worldcup.tracker.dto.TeamDTO;
import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.repository.EquipeRepository;
import org.springframework.stereotype.Service;
import com.worldcup.tracker.mapper.TeamMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService implements ITeamService {

    private final EquipeRepository equipeRepository;

    public TeamService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return equipeRepository.findAll().stream()
                .map(TeamMapper::toDTO)
                .toList();
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setFlagUrl(team.getFlagUrl());
        return dto;
    }
}
