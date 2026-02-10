package com.worldcup.tracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.dto.EquipeDTO;
import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.repository.EquipeRepository;

@Service
public class EquipeServiceImpl implements IEquipeService {

    private final EquipeRepository equipeRepository;

    public EquipeServiceImpl(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Override
    public Equipe getEntityById(Long id) {
        return equipeRepository.findById(id).orElse(null);
    }

    @Override
    public List<EquipeDTO> getAll() {
        return equipeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipeDTO> getByGroupe(String groupe) {
        return equipeRepository.findByGroupeNom(groupe).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipeDTO> getQualifiedTeams() {
        // For now return empty list - implement logic as needed
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

    private EquipeDTO mapToDTO(Equipe equipe) {
        return EquipeDTO.builder()
                .id(equipe.getId())
                .nom(equipe.getNom())
                .drapeauUrl(equipe.getDrapeauUrl()) 
                .codePays(equipe.getCodePays())
                .groupeCode(equipe.getGroupeCode())
                .fifaRanking(equipe.getFifaRanking())
                .confederation(equipe.getConfederation())
                .build();
    }
}