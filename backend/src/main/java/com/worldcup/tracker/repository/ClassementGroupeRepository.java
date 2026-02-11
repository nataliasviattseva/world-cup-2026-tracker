package com.worldcup.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.ClassementGroupe;

public interface ClassementGroupeRepository extends JpaRepository<ClassementGroupe, Long> {

    List<ClassementGroupe> findByGroupeNomOrderByPointsDescDifferenceButsDesc(String groupeNom);

    Optional<ClassementGroupe> findByGroupeNomAndEquipeId(String groupeNom, Long equipeId);

    List<ClassementGroupe> findByGroupeNomOrderByPositionAsc(String groupeNom);

}
