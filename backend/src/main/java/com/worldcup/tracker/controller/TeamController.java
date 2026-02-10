package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.service.EquipeService;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    private final EquipeService equipeService;

    public TeamController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public List<Equipe> getAllTeams() {
        return equipeService.getAll();
    }

    @GetMapping("/{id}")
    public Equipe getTeamById(@PathVariable Long id) {
        return equipeService.getEntityById(id);
    }
}