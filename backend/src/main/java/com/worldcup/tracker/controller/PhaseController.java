package com.worldcup.tracker.controller;

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

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GetMapping
    public List<PhaseDTO> getAllPhases() {
        return phaseService.getAllPhases();
    }
}
