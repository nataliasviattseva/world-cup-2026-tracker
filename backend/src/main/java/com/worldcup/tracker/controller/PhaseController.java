package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.service.PhaseCompetitionService;
import com.worldcup.tracker.dto.PhaseDTO;
import com.worldcup.tracker.service.PhaseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
