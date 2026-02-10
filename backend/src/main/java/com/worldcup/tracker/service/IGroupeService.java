package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.model.Groupe;

public interface IGroupeService {

    Groupe getEntityById(Long id);

    List<Groupe> getAll();

    Groupe getByNom(String nom);

    List<Groupe> getByPhase(Long phaseId);

    Groupe save(Groupe groupe);

    void delete(Long id);

    List<Groupe> getAllOrderedByNom();

    boolean existsByNom(String nom);

    void updateGroupeStatistics(String groupeNom);
}