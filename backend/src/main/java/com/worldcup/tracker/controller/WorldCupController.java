package com.worldcup.tracker.controller;

import com.worldcup.tracker.service.external.WorldCupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/worldcup")
@RequiredArgsConstructor
public class WorldCupController {

    private final WorldCupService worldCupService;

    @GetMapping("/matches")
    public Object getMatches() {
        return worldCupService.getAllMatches();
    }

    @GetMapping("/team/{id}")
    public Object getTeamMatches(@PathVariable int id) {
        return worldCupService.getTeamMatches(id);
    }
}
