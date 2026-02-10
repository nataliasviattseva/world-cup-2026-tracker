package com.worldcup.tracker.controller;

import com.worldcup.tracker.dto.TeamDTO;
import com.worldcup.tracker.service.ITeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    private final ITeamService ITeamService;

    public TeamController(ITeamService ITeamService) {
        this.ITeamService = ITeamService;
    }

    @GetMapping
    public List<TeamDTO> getAllTeams() {
        return ITeamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamDTO getTeamById(@PathVariable Long id) {
        return ITeamService.getTeamById(id);
    }
}