package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.dto.EquipeDTO;

public interface IEquipeService {

    Equipe getEntityById(Long id);

    List<EquipeDTO> getAll();

    List<EquipeDTO> getByGroupe(String groupe);

    List<EquipeDTO> getQualifiedTeams();

    Equipe getByNom(String nom);

    List<Equipe> getByPays(String pays);

    Equipe save(Equipe equipe);

    void delete(Long id);

    List<Equipe> getTopTeamsByPoints(int limit);

    List<Equipe> getByGroupeOrderedByPoints(String groupe);

}