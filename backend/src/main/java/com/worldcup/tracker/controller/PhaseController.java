package com.worldcup.tracker.controller;

import java.util.List;

<<<<<<< HEAD
import com.worldcup.tracker.dto.PhaseCompetitionDTO;
import com.worldcup.tracker.service.IPhaseService;
=======
import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.service.PhaseCompetitionService;
import org.springframework.web.bind.annotation.CrossOrigin;
>>>>>>> 9a381468b38f76c71854414351345a9d0529e825
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
