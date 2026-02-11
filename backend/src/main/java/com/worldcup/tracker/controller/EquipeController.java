package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldcup.tracker.dto.EquipeDTO;
import com.worldcup.tracker.service.IEquipeService;

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
        return equipeService.getById(id);
    }
}
