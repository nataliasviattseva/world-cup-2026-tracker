package com.worldcup.tracker.controller;

import com.worldcup.tracker.service.external.FootballApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchSyncController {

    private final FootballApiService footballApiService;

    @GetMapping("/external/{id}")
    public String getMatchFromApi(@PathVariable Long id) {
        return footballApiService.getMatch(id);
    }
}
