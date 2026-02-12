package com.worldcup.tracker.controller;

import java.util.List;

import com.worldcup.tracker.dto.PhaseCompetitionDTO;
import com.worldcup.tracker.service.IPhaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phases")
public class PhaseController {

    private final IPhaseService phaseService;

    public PhaseController(IPhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GetMapping
    public List<PhaseCompetitionDTO> getAllPhases() {
        return phaseService.getAllPhases();
    }
}
