package com.worldcup.tracker.controller;

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
