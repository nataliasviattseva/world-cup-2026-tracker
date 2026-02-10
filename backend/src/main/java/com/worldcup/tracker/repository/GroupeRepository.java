package com.worldcup.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.Groupe;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    Optional<Groupe> findByLettreIgnoreCase(String lettre);

    Optional<Groupe> findByNom(String nom);
    
    List<Groupe> findAllByOrderByNomAsc();
    
    boolean existsByNom(String nom);

}
