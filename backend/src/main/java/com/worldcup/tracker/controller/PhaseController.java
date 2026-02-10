package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.service.PhaseCompetitionService;

@RestController
@RequestMapping("/api/phases")
@CrossOrigin(origins = "*")
public class PhaseController {

    private final PhaseCompetitionService phaseCompetitionService;

    public PhaseController(PhaseCompetitionService phaseCompetitionService) {
        this.phaseCompetitionService = phaseCompetitionService;
    }

    @GetMapping
    public List<PhaseCompetition> getAllPhases() {
        return phaseCompetitionService.getAll();
    }
}
