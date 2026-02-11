package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.service.IPhaseCompetitionService;
import com.worldcup.tracker.dto.PhaseCompetitionDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phases")
@CrossOrigin(origins = "*")
public class PhaseController {

    private final IPhaseCompetitionService phaseCompetitionService;

    public PhaseController(IPhaseCompetitionService phaseCompetitionService) {
        this.phaseCompetitionService = phaseCompetitionService;
    }

    @GetMapping
    public List<PhaseCompetition> getAllPhases() {
        return phaseCompetitionService.getAll();
    }
}
