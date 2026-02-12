package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.ClassementGroupe;
import com.worldcup.tracker.service.IClassementGroupeService;

@RestController
@RequestMapping("/api/standings")
public class ClassementGroupeController {

    private final IClassementGroupeService classementService;

    public ClassementGroupeController(IClassementGroupeService classementService) {
        this.classementService = classementService;
    }

    @GetMapping
    public List<ClassementGroupe> getAllStandings() {
        return classementService.getAll();
    }

    @GetMapping("/{id}")
    public ClassementGroupe getStandingById(@PathVariable Long id) {
        return classementService.getEntityById(id);
    }

    @GetMapping("/group/{groupeName}")
    public List<ClassementGroupe> getStandingsByGroup(@PathVariable String groupeName) {
        return classementService.getByGroupe(groupeName);
    }

    @GetMapping("/group/{groupeName}/ordered")
    public List<ClassementGroupe> getStandingsByGroupOrdered(@PathVariable String groupeName) {
        return classementService.getByGroupeOrderedByPosition(groupeName);
    }

    @GetMapping("/group/{groupeName}/qualified/{count}")
    public List<ClassementGroupe> getQualifiedTeams(
            @PathVariable String groupeName, 
            @PathVariable int count) {
        return classementService.getQualifiedTeamsFromGroupe(groupeName, count);
    }

    @GetMapping("/group/{groupeName}/team/{equipeId}")
    public ClassementGroupe getTeamStanding(
            @PathVariable String groupeName, 
            @PathVariable Long equipeId) {
        return classementService.getByGroupeAndEquipe(groupeName, equipeId);
    }

    @PostMapping
    public ClassementGroupe createStanding(@RequestBody ClassementGroupe classement) {
        return classementService.save(classement);
    }

    @PutMapping("/{id}")
    public ClassementGroupe updateStanding(@PathVariable Long id, @RequestBody ClassementGroupe classement) {
        classement.setId(id);
        return classementService.save(classement);
    }

    @PutMapping("/group/{groupeName}/update")
    public void updateGroupStandings(@PathVariable String groupeName) {
        classementService.updateClassement(groupeName);
    }

    @DeleteMapping("/{id}")
    public void deleteStanding(@PathVariable Long id) {
        classementService.delete(id);
    }
}