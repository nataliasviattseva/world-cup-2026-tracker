package com.worldcup.tracker.controller;

import java.util.List;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.service.IEquipeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    private final IEquipeService equipeService;

    public TeamController(IEquipeService equipeService) {
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
