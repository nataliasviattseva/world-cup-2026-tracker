package com.worldcup.tracker.service.impl;

import java.util.List;

import com.worldcup.tracker.service.EquipeService;
import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.repository.EquipeRepository;

@Service
public class EquipeServiceImpl implements EquipeService {

    private final EquipeRepository equipeRepository;

    public EquipeServiceImpl(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Override
    public Equipe getEntityById(Long id) {
        return equipeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Equipe> getAll() {
        return equipeRepository.findAll();
    }

    @Override
    public List<Equipe> getByGroupe(String groupe) {
        return equipeRepository.findByGroupeNom(groupe);
    }

    @Override
    public List<Equipe> getQualifiedTeams() {
        return List.of();
    }

    @Override
    public Equipe getByNom(String nom) {
        return equipeRepository.findByNomIgnoreCase(nom).orElse(null);
    }

    @Override
    public List<Equipe> getByPays(String pays) {
        return equipeRepository.findByCodePaysIgnoreCase(pays).map(List::of).orElse(List.of());
    }

    @Override
    public Equipe save(Equipe equipe) {
        return equipeRepository.save(equipe);
    }

    @Override
    public void delete(Long id) {
        equipeRepository.deleteById(id);
    }

    @Override
    public List<Equipe> getTopTeamsByPoints(int limit) {
        return equipeRepository.findAllByOrderByNomAsc().stream().limit(limit).toList();
    }

    @Override
    public List<Equipe> getByGroupeOrderedByPoints(String groupe) {
        return equipeRepository.findByGroupeNomOrderByNomAsc(groupe);
    }
}