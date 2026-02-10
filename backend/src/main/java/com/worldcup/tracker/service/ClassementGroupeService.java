package com.worldcup.tracker.service;

import java.util.List;

import com.worldcup.tracker.model.ClassementGroupe;

public interface ClassementGroupeService {

    ClassementGroupe getEntityById(Long id);

    List<ClassementGroupe> getAll();

    List<ClassementGroupe> getByGroupe(String groupeNom);

    ClassementGroupe getByGroupeAndEquipe(String groupeNom, Long equipeId);

    ClassementGroupe save(ClassementGroupe classement);

    void delete(Long id);

    void updateClassement(String groupeNom);

    List<ClassementGroupe> getByGroupeOrderedByPosition(String groupeNom);

    List<ClassementGroupe> getQualifiedTeamsFromGroupe(String groupeNom, int qualifiedCount);
}