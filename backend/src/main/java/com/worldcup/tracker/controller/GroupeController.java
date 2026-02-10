package com.worldcup.tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.worldcup.tracker.model.Groupe;
import com.worldcup.tracker.service.GroupeService;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupeController {

    private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping
    public List<Groupe> getAllGroups() {
        return groupeService.getAll();
    }

    @GetMapping("/{id}")
    public Groupe getGroupById(@PathVariable Long id) {
        return groupeService.getEntityById(id);
    }

    @GetMapping("/name/{nom}")
    public Groupe getGroupByName(@PathVariable String nom) {
        return groupeService.getByNom(nom);
    }

    @GetMapping("/phase/{phaseId}")
    public List<Groupe> getGroupsByPhase(@PathVariable Long phaseId) {
        return groupeService.getByPhase(phaseId);
    }

    @GetMapping("/ordered")
    public List<Groupe> getAllGroupsOrdered() {
        return groupeService.getAllOrderedByNom();
    }

    @GetMapping("/exists/{nom}")
    public boolean checkGroupExists(@PathVariable String nom) {
        return groupeService.existsByNom(nom);
    }

    @PostMapping
    public Groupe createGroup(@RequestBody Groupe groupe) {
        return groupeService.save(groupe);
    }

    @PutMapping("/{id}")
    public Groupe updateGroup(@PathVariable Long id, @RequestBody Groupe groupe) {
        groupe.setId(id);
        return groupeService.save(groupe);
    }

    @PutMapping("/name/{nom}/update-statistics")
    public void updateGroupStatistics(@PathVariable String nom) {
        groupeService.updateGroupeStatistics(nom);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupeService.delete(id);
    }
}