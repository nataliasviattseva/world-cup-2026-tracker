package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.model.Equipe;

public interface IEquipeService {

    Equipe getEntityById(Long id);

    List<Equipe> getAll();

    List<Equipe> getByGroupe(String groupe);

    List<Equipe> getQualifiedTeams();

    Equipe getByNom(String nom);

    List<Equipe> getByPays(String pays);

    Equipe save(Equipe equipe);

    void delete(Long id);

    List<Equipe> getTopTeamsByPoints(int limit);

    List<Equipe> getByGroupeOrderedByPoints(String groupe);
}