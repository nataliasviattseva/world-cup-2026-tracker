package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.Equipe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    Optional<Equipe> findByNomIgnoreCase(String nom);

    Optional<Equipe> findByCodePaysIgnoreCase(String codePays);

    List<Equipe> findByGroupeId(Long groupeId);

}
