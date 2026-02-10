package com.worldcup.tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.Groupe;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    Optional<Groupe> findByLettreIgnoreCase(String lettre);

}
