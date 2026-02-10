package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.service.IEquipeService;
import com.worldcup.tracker.dto.EquipeDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class EquipeController {

    private final IEquipeService equipeService;

    public EquipeController(IEquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public List<EquipeDTO> getAllTeams() {
        return equipeService.getAll();
    }

    @GetMapping("/{id}")
    public EquipeDTO getTeamById(@PathVariable Long id) {
        // This will need a new method in the service to return DTO
        Equipe equipe = equipeService.getEntityById(id);
        // For now, create a simple mapping or add a method to service
        return EquipeDTO.builder()
                .id(equipe.getId())
                .nom(equipe.getNom())
                .codePays(equipe.getCodePays())
                .drapeauUrl(equipe.getDrapeauUrl())
                .groupeCode(equipe.getGroupeCode())
                .fifaRanking(equipe.getFifaRanking())
                .confederation(equipe.getConfederation())
                .build();
    }
}
