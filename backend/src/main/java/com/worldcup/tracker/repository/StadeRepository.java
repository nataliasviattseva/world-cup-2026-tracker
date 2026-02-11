package com.worldcup.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.Stade;

public interface StadeRepository extends JpaRepository<Stade, Long> {

    Optional<Stade> findByNomIgnoreCase(String nom);

    List<Stade> findByPaysIgnoreCase(String pays);
}
